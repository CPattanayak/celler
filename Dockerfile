FROM alpine:3.7
RUN mkdir /app 
WORKDIR /app
COPY . .

RUN chmod 0700 ./golang-build
CMD ["./golang-build"]
EXPOSE 8081