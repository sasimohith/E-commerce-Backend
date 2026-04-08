# E-commerce Backend Deployment Guide

This guide provides step-by-step instructions for deploying the E-commerce Backend in different environments.

## 🛠️ Prerequisites

### Development Environment
- **Java**: 17 or higher (LTS recommended)
- **Maven**: 3.6+ 
- **MySQL**: 8.x
- **Git**: Latest version
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code

### Production Environment  
- **Java**: 17+ (OpenJDK recommended)
- **MySQL**: 8.x (or compatible cloud database)
- **Reverse Proxy**: Nginx (recommended)
- **SSL Certificate**: Let's Encrypt or commercial
- **Process Manager**: systemd, pm2, or Docker

## 🔧 Development Setup

### 1. Clone and Setup
```bash
# Clone the repository
git clone <your-repository-url>
cd ecommerce-backend

# Copy environment template
cp .env.example .env

# Edit environment variables
vim .env  # or your preferred editor
```

### 2. Database Setup
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE ecommercedb;
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON ecommercedb.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Configure Application
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommercedb
    username: ecommerce_user  # or use environment variable
    password: ${DB_PASSWORD}
```

### 4. Build and Run
```bash
# Build the project
mvn clean compile

# Run tests
mvn test

# Start application
mvn spring-boot:run

# Access application
curl http://localhost:8080/actuator/health
```

## 🌐 Production Deployment

### Option 1: Traditional Server Deployment

#### 1. Server Preparation
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk

# Install MySQL
sudo apt install mysql-server
sudo mysql_secure_installation

# Create application user
sudo useradd -r -m -s /bin/bash ecommerce
sudo mkdir -p /opt/ecommerce
sudo chown ecommerce:ecommerce /opt/ecommerce
```

#### 2. Application Deployment
```bash
# Build production JAR
mvn clean package -DskipTests

# Copy JAR to server
scp target/ecommerce-backend-0.0.1-SNAPSHOT.jar user@server:/opt/ecommerce/

# Create application.yml for production
sudo tee /opt/ecommerce/application.yml > /dev/null <<EOF
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommercedb
    username: ecommerce_user
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate  # Use validate in production
    show-sql: false       # Disable SQL logging in production
logging:
  level:
    com.example.ecommerce: INFO
    org.springframework.security: WARN
EOF
```

#### 3. Systemd Service Setup
```bash
# Create systemd service
sudo tee /etc/systemd/system/ecommerce.service > /dev/null <<EOF
[Unit]
Description=E-commerce Backend API
After=mysql.service

[Service]
Type=simple
User=ecommerce
Group=ecommerce
WorkingDirectory=/opt/ecommerce
ExecStart=/usr/bin/java -jar -Dspring.config.location=application.yml ecommerce-backend-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
Environment=DB_PASSWORD=your_secure_password

[Install]
WantedBy=multi-user.target
EOF

# Enable and start service
sudo systemctl daemon-reload
sudo systemctl enable ecommerce
sudo systemctl start ecommerce
sudo systemctl status ecommerce
```

#### 4. Nginx Reverse Proxy
```bash
# Install Nginx
sudo apt install nginx

# Create site configuration
sudo tee /etc/nginx/sites-available/ecommerce > /dev/null <<EOF
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

# Enable site
sudo ln -s /etc/nginx/sites-available/ecommerce /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### Option 2: Docker Deployment

#### 1. Create Dockerfile
```dockerfile
FROM openjdk:17-jdk-alpine

# Create app directory
WORKDIR /app

# Copy JAR file
COPY target/ecommerce-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 2. Docker Compose Setup
```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: ecommercedb
      MYSQL_USER: ecommerce_user
      MYSQL_PASSWORD: ${DB_PASSWORD}
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 10

  ecommerce-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_PASSWORD=${DB_PASSWORD}
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/ecommercedb
    depends_on:
      mysql:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./ssl:/etc/ssl/certs
    depends_on:
      - ecommerce-api

volumes:
  mysql_data:
```

#### 3. Deploy with Docker
```bash
# Build and deploy
docker-compose up --build -d

# View logs
docker-compose logs -f ecommerce-api

# Scale if needed
docker-compose up --scale ecommerce-api=2 -d
```

## 🔒 Security Hardening

### 1. Environment Variables
```bash
# Create secure environment file
sudo tee /opt/ecommerce/.env > /dev/null <<EOF
DB_PASSWORD=very_secure_password_here
JWT_SECRET=super_long_random_jwt_secret_key_here_256_bits_minimum
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
EOF

# Secure permissions
sudo chown ecommerce:ecommerce /opt/ecommerce/.env
sudo chmod 600 /opt/ecommerce/.env
```

### 2. Database Security
```sql
-- Create dedicated database user
CREATE USER 'ecommerce_prod'@'localhost' IDENTIFIED BY 'very_secure_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON ecommercedb.* TO 'ecommerce_prod'@'localhost';

-- Remove test data in production
DELETE FROM users WHERE email LIKE '%example.com';
```

### 3. Application Security Updates
```yaml
# application-prod.yml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # Never use 'update' in production
    show-sql: false
logging:
  level:
    com.example.ecommerce: INFO
    org.springframework.security: WARN
management:
  endpoints:
    web:
      exposure:
        include: health,info  # Limit exposed endpoints
```

## 📊 Monitoring & Maintenance

### 1. Health Monitoring
```bash
# Check application health
curl http://localhost:8080/actuator/health

# Monitor logs
sudo journalctl -u ecommerce -f

# Check system resources
htop
df -h
```

### 2. Database Maintenance
```sql
-- Regular maintenance
ANALYZE TABLE users, products, orders, carts;
OPTIMIZE TABLE users, products, orders, carts;

-- Monitor database size
SELECT 
    table_schema AS 'Database',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables
WHERE table_schema = 'ecommercedb'
GROUP BY table_schema;
```

### 3. Backup Strategy
```bash
#!/bin/bash
# backup-script.sh
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/opt/backups"

# Database backup
mysqldump -u root -p ecommercedb > $BACKUP_DIR/ecommerce_$DATE.sql

# Application backup
tar -czf $BACKUP_DIR/ecommerce_app_$DATE.tar.gz /opt/ecommerce

# Keep only last 7 days
find $BACKUP_DIR -name "ecommerce_*" -mtime +7 -delete

# Crontab entry: 0 2 * * * /opt/scripts/backup-script.sh
```

## 🚀 Performance Optimization

### 1. JVM Tuning
```bash
# Production JVM options
JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### 2. Database Optimization
```sql
-- Add performance indexes
CREATE INDEX idx_products_name_price ON products(name, price);
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- MySQL configuration
[mysqld]
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M
query_cache_type = 1
query_cache_size = 32M
```

## 🔄 CI/CD Pipeline

### GitHub Actions Example
```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'adopt'
    
    - name: Build with Maven
      run: mvn clean package -DskipTests
    
    - name: Deploy to server
      run: |
        scp target/*.jar user@server:/opt/ecommerce/
        ssh user@server 'sudo systemctl restart ecommerce'
```

## 📋 Deployment Checklist

### Pre-deployment
- [ ] Environment variables configured
- [ ] Database created and secured
- [ ] SSL certificates obtained
- [ ] Firewall rules configured
- [ ] Backup strategy implemented
- [ ] Monitoring setup complete

### Post-deployment
- [ ] Health checks passing
- [ ] Database connectivity verified
- [ ] API endpoints responding
- [ ] Frontend integration tested
- [ ] SSL/TLS working correctly
- [ ] Logs monitoring functional
- [ ] Performance benchmarks met

## 🆘 Troubleshooting

### Common Issues

#### Application Won't Start
```bash
# Check logs
sudo journalctl -u ecommerce -n 50

# Check Java version
java -version

# Check port availability
netstat -tulpn | grep 8080
```

#### Database Connection Issues
```bash
# Test database connection
mysql -u ecommerce_user -p -h localhost ecommercedb

# Check MySQL status
sudo systemctl status mysql

# Review MySQL logs
sudo tail -f /var/log/mysql/error.log
```

#### High Memory Usage
```bash
# Check JVM memory
jstat -gc <java_pid>

# Adjust JVM settings
# Edit /etc/systemd/system/ecommerce.service
Environment="JAVA_OPTS=-Xms256m -Xmx1g"
```

---

## 📞 Support

For deployment issues:
1. Check application logs: `sudo journalctl -u ecommerce -f`
2. Verify database connectivity
3. Confirm environment variables are set
4. Review security group/firewall settings
5. Test API endpoints manually

This deployment guide ensures your E-commerce Backend runs reliably in production with proper security, monitoring, and maintenance procedures.