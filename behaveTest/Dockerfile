FROM alpine:3.7

RUN apk add --update \
    python3 \
    python3-dev \
    py-pip \
    build-base \
    openjdk8-jre \

  && pip install virtualenv \
  && rm -rf /var/cache/apk/*
RUN pip install allure-behave
RUN pip install jsonschema
RUN pip install requests
RUN pip install urllib3
RUN pip install selenium
RUN pip install xvfbwrapper

RUN mkdir /opt

RUN wget https://dl.bintray.com/qameta/generic/io/qameta/allure/allure/2.6.0/allure-2.6.0.tgz
RUN tar -zxvf allure-2.6.0.tgz -C /opt/
RUN ln -s /opt/allure-2.6.0/bin/allure /usr/bin/allure
RUN allure --version

RUN apk update && apk add --no-cache bash \
    alsa-lib \
    at-spi2-atk \
    atk \
    cairo \
    cups-libs \
    dbus-libs \
    eudev-libs \
    expat \
    flac \
    gdk-pixbuf \
    glib \
    libgcc \
    libjpeg-turbo \
    libpng \
    libwebp \
    libx11 \
    libxcomposite \
    libxdamage \
    libxext \
    libxfixes \
    tzdata \
    libexif \
    udev \
    xvfb \
    zlib-dev \
    chromium \
    chromium-chromedriver \
	&& rm -rf /var/cache/apk/*