FROM amazoncorretto:8u402-al2023

ARG USERNAME
ARG USER_UID
ARG USER_GID
ARG TIME_ZONE
ARG LOCALE
ARG WORKING_DIR

ENV LANG=${LOCALE}
ENV TZ=${TIME_ZONE}

RUN dnf install -y sudo git findutils tar

RUN dnf install -y glibc-langpack-ja \
  && ln -snf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime \
  && echo ${TIME_ZONE} > /etc/timezone 

RUN /usr/sbin/groupadd --gid ${USER_GID} ${USERNAME} \
  && /usr/sbin/useradd -s /bin/bash --uid ${USER_UID} --gid ${USER_GID} -m ${USERNAME} \
  && echo ${USERNAME} ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/${USERNAME} \
  && chmod 0440 /etc/sudoers.d/${USERNAME}

USER ${USER_UID}

RUN curl https://get.volta.sh | bash

RUN /bin/echo -e "\n\
LANG=\"${LANG}\"\n\
LC_ALL=\"${LOCALE}\"\n\
" >> ~/.bashrc

RUN sudo rm -rf /var/task \
  && sudo ln -s ${WORKING_DIR} /var/task
