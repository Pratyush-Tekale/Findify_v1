CREATE DATABASE findify_db;
USE findify_db;

CREATE TABLE users(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER','ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories(
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO categories(category_name)
VALUES
('Electronics'),
('Books'),
('Wallet'),
('ID Card'),
('Keys'),
('Bag'),
('Clothing'),
('Mobile'),
('Jewellery'),
('Accessories'),
('Others');


CREATE TABLE lost_items(
    lost_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    location_lost VARCHAR(150) NOT NULL,
    date_lost DATE NOT NULL,
    image VARCHAR(255),
    status ENUM('OPEN','FOUND','CLAIMED')
    DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		CONSTRAINT fk_lost_user
		FOREIGN KEY(user_id)
		REFERENCES users(user_id)
		ON DELETE CASCADE,
		CONSTRAINT fk_lost_category
		FOREIGN KEY(category_id)
		REFERENCES categories(category_id)
);


CREATE TABLE found_items(
    found_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    location_found VARCHAR(150) NOT NULL,
    date_found DATE NOT NULL,
    image VARCHAR(255),
    status ENUM('UNCLAIMED','CLAIMED')
    DEFAULT 'UNCLAIMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		CONSTRAINT fk_found_user
		FOREIGN KEY(user_id)
		REFERENCES users(user_id)
		ON DELETE CASCADE,
		CONSTRAINT fk_found_category
		FOREIGN KEY(category_id)
		REFERENCES categories(category_id)
);



CREATE TABLE claims(
    claim_id INT AUTO_INCREMENT PRIMARY KEY,
    found_id INT NOT NULL,
    claimant_id INT NOT NULL,
    proof TEXT NOT NULL,
    status ENUM('PENDING','APPROVED','REJECTED')
    DEFAULT 'PENDING',
    claim_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		CONSTRAINT fk_claim_found
		FOREIGN KEY(found_id)
		REFERENCES found_items(found_id)
		ON DELETE CASCADE,
		CONSTRAINT fk_claim_user
		FOREIGN KEY(claimant_id)
		REFERENCES users(user_id)
		ON DELETE CASCADE
);


CREATE TABLE notifications(
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		CONSTRAINT fk_notification_user
		FOREIGN KEY(user_id)
		REFERENCES users(user_id)
		ON DELETE CASCADE
);