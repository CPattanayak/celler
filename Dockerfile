FROM alpine:3.7
RUN mkdir /app 
COPY golang-build /app/
WORKDIR /app
RUN chmod 0700 /golang-build
CMD ["/app/golang-build"]
EXPOSE 8081