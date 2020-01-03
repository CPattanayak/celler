FROM alpine:3.7
RUN mkdir /app 
WORKDIR /app
COPY golang-build .

RUN chmod 0700 ./golang-build
CMD ["./golang-build"]
EXPOSE 8082