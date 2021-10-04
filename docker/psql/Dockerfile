FROM postgres:9.6.23

RUN localedef -i pl_PL -c -f UTF-8 -A /usr/share/locale/locale.alias pl_PL.UTF-8
ENV LANG pl_PL.UTF-8

RUN sed -i 's/^# *\(pl_PL.UTF-8\|pl_PL.UTF-8\)/\1/' /etc/locale.gen && \
        locale-gen

RUN rm /etc/localtime \
    && ln -s /usr/share/zoneinfo/Europe/Warsaw /etc/localtime \
    && locale-gen --purge pl_PL.UTF-8 \
