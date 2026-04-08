-- Sample data for testing the e-commerce backend

-- Insert Categories
INSERT INTO categories (name, description) VALUES 
('Electronics', 'Electronic devices and gadgets'),
('Books', 'Books and e-books'),
('Clothing', 'Apparel and accessories'),
('Home & Garden', 'Home improvement and garden supplies'),
('Sports', 'Sports equipment and accessories');

-- Insert Products
INSERT INTO products (name, description, price, stock_quantity, sku, image_url, is_active, category_id, created_at, updated_at) VALUES 
('Laptop Pro 15"', 'High-performance laptop with 16GB RAM and 512GB SSD', 1299.99, 25, 'LAP-PRO-15', 'https://example.com/laptop-pro.jpg', true, 1, NOW(), NOW()),
('Wireless Headphones', 'Noise-canceling wireless headphones with 30-hour battery', 199.99, 50, 'WH-NC-30', 'https://example.com/headphones.jpg', true, 1, NOW(), NOW()),
('Smartphone X', 'Latest smartphone with advanced camera and fast processor', 899.99, 30, 'PHONE-X', 'https://example.com/phone-x.jpg', true, 1, NOW(), NOW()),
('Programming Book', 'Learn modern programming techniques', 49.99, 100, 'BOOK-PROG', 'https://example.com/prog-book.jpg', true, 2, NOW(), NOW()),
('Winter Jacket', 'Warm winter jacket for cold weather', 129.99, 40, 'JAC-WINTER', 'https://example.com/winter-jacket.jpg', true, 3, NOW(), NOW()),
('Garden Tools Set', 'Complete set of essential garden tools', 79.99, 20, 'GARDEN-SET', 'https://example.com/garden-tools.jpg', true, 4, NOW(), NOW()),
('Tennis Racket', 'Professional tennis racket for competitive play', 159.99, 15, 'TENNIS-PRO', 'https://example.com/tennis-racket.jpg', true, 5, NOW(), NOW()),
('Running Shoes', 'Comfortable running shoes for long distances', 119.99, 60, 'SHOES-RUN', 'https://example.com/running-shoes.jpg', true, 5, NOW(), NOW());

-- Insert a test user (password is 'password123' encoded with BCrypt)
-- Note: In a real application, you would register users through the API
INSERT INTO users (name, email, password, phone, address, city, state, zip_code, country) VALUES 
('John Doe', 'john.doe@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFw4OzklYNBpJZ5gJCE7rh6', '+1234567890', '123 Main Street', 'New York', 'NY', '10001', 'USA'),
('Jane Smith', 'jane.smith@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFw4OzklYNBpJZ5gJCE7rh6', '+0987654321', '456 Oak Avenue', 'Los Angeles', 'CA', '90210', 'USA');
