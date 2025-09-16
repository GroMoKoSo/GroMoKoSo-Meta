FROM alpine:latest

# Install busybox httpd (tiny webserver)
RUN apk add --no-cache busybox-extras

# Create a simple HTML file
RUN echo 'Hello, World!' > /var/www/index.html

# Expose port 8080
EXPOSE 8080

# Run the webserver
CMD ["httpd", "-f", "-p", "8080"]
