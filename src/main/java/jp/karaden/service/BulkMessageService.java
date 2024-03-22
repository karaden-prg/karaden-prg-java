package jp.karaden.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import jp.karaden.RequestOptions;
import jp.karaden.Utility;
import jp.karaden.exception.BulkMessageCreateFailedException;
import jp.karaden.exception.BulkMessageListMessageRetryLimitExceedException;
import jp.karaden.exception.BulkMessageShowRetryLimitExceedException;
import jp.karaden.exception.FileDownloadFailedException;
import jp.karaden.exception.FileNotFoundException;
import jp.karaden.exception.KaradenException;
import jp.karaden.model.BulkFile;
import jp.karaden.model.BulkMessage;
import jp.karaden.param.message.bulk.BulkMessageCreateParams;
import jp.karaden.param.message.bulk.BulkMessageDownloadParams;
import jp.karaden.param.message.bulk.BulkMessageListMessageParams;
import jp.karaden.param.message.bulk.BulkMessageShowParams;

public class BulkMessageService {
    public final static int BUFFER_SIZE = 1024 * 1024;
    public final static String REGEX_PATTERN = "filename=\"([^\"]+)\"";

    public static BulkMessage create(String filename) throws KaradenException, FileNotFoundException {
        File file = new File(filename);
        if (!file.isFile()) {
            throw new FileNotFoundException();
        }

        BulkFile bulkFile = BulkFile.create();

        Utility.putSignedUrl(bulkFile.getUrl(), filename, "text/csv");

        BulkMessageCreateParams params = BulkMessageCreateParams.newBuilder()
            .withBulkFileId((String)bulkFile.getId())
            .build();
        return BulkMessage.create(params);
    }

    public static BulkMessage create(String filename, RequestOptions requestOptions) throws KaradenException, FileNotFoundException {
        File file = new File(filename);
        if (!file.isFile()) {
            throw new FileNotFoundException();
        }

        BulkFile bulkFile = BulkFile.create(requestOptions);

        Utility.putSignedUrl(bulkFile.getUrl(), filename, "text/csv", requestOptions);

        BulkMessageCreateParams params = BulkMessageCreateParams.newBuilder()
            .withBulkFileId((String)bulkFile.getId())
            .build();
        return BulkMessage.create(params, requestOptions);
    }

    public static boolean download(BulkMessageDownloadParams params, RequestOptions requestOptions) throws KaradenException, BulkMessageCreateFailedException, BulkMessageListMessageRetryLimitExceedException, BulkMessageShowRetryLimitExceedException, InterruptedException {
        params.validate();
        BulkMessageShowParams showParams = BulkMessageShowParams.newBuilder()
            .withId(params.id)
            .build();

        if (!BulkMessageService.checkBulkMessageStatus(params.maxRetries, params.retryInterval, showParams, requestOptions)) {
            throw new BulkMessageShowRetryLimitExceedException();
        }

        BulkMessageListMessageParams listParams = BulkMessageListMessageParams.newBuilder()
            .withId(params.id)
            .build();

        String downloadUrl = BulkMessageService.getDownloadUrl(params.maxRetries, params.retryInterval, listParams, requestOptions);
        if (Objects.isNull(downloadUrl)) {
            throw new BulkMessageListMessageRetryLimitExceedException();
        }

        try {
            BulkMessageService.getContents(downloadUrl, Paths.get(params.directoryPath).toRealPath().toString(), requestOptions);
        } catch (IOException e) {
            throw new FileDownloadFailedException();
        }

        return true;
    }

    private static void getContents(String downloadUrl, String directoryPath, RequestOptions requestOptions) throws IOException, FileDownloadFailedException {
        HashMap<String, Integer> timeout = Utility.getTimeout(requestOptions);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(timeout.get("connectionTimeout"), TimeUnit.MILLISECONDS)
                .readTimeout(timeout.get("readTimeout"), TimeUnit.MILLISECONDS)
                .build();
        okhttp3.Request request = (new okhttp3.Request.Builder())
            .url(downloadUrl)
            .build();

        Response response = client.newCall(request).execute();
        String header =  response.headers().get("content-disposition");
        Pattern pattern = Pattern.compile(BulkMessageService.REGEX_PATTERN);
        Matcher match = pattern.matcher(header);
        if (!match.find()) {
            throw new FileDownloadFailedException();
        }
        String filename =  match.group(1);
        String filePath = FileSystems.getDefault().getPath(directoryPath, filename).toString();
        BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
        try (
            BufferedOutputStream  outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        ) {
            byte[] buffer = new byte[BulkMessageService.BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private static boolean checkBulkMessageStatus(Integer retryCount, Integer retryInterval, BulkMessageShowParams params, RequestOptions requestOptions) throws KaradenException, BulkMessageCreateFailedException, InterruptedException {
        for (int count = 0; count < retryCount + 1; count++) {
            if(count > 0) {
                Thread.sleep(retryInterval * 1000);
            }

            BulkMessage bulkMessage = BulkMessage.show(params, requestOptions);
            if (bulkMessage.getStatus().equals(BulkMessage.STATUS_ERROR)) {
                throw new BulkMessageCreateFailedException();
            }

            if (bulkMessage.getStatus().equals(BulkMessage.STATUS_DONE)) {
                return true;
            }
        }

        return false;
    }

    private static String getDownloadUrl(Integer retryCount, Integer retryInterval, BulkMessageListMessageParams params, RequestOptions requestOptions) throws KaradenException, InterruptedException {
        for (int count = 0; count < retryCount + 1; count++) {
            if(count > 0) {
                Thread.sleep(retryInterval * 1000);
            }

            String result = BulkMessage.listMessage(params, requestOptions);
            if(!Objects.isNull(result))
                return result;
        }

        return null;
    }
}
