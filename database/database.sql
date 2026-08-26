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



-- Private verification questions set by the finder when a found item is
-- reported. Answers are NEVER shown on the public Found Items page and are
-- only used server-side to score a claimant's submitted answers.
CREATE TABLE verification_questions(
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    found_id INT NOT NULL,
    question_text VARCHAR(255) NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
		CONSTRAINT fk_vq_found
		FOREIGN KEY(found_id)
		REFERENCES found_items(found_id)
		ON DELETE CASCADE
);

CREATE TABLE claims(
    claim_id INT AUTO_INCREMENT PRIMARY KEY,
    found_id INT NOT NULL,
    claimant_id INT NOT NULL,
    status ENUM('PENDING','APPROVED','REJECTED')
    DEFAULT 'PENDING',
    claim_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    matched_answers INT DEFAULT 0,
    total_questions INT DEFAULT 0,
		CONSTRAINT fk_claim_found
		FOREIGN KEY(found_id)
		REFERENCES found_items(found_id)
		ON DELETE CASCADE,
		CONSTRAINT fk_claim_user
		FOREIGN KEY(claimant_id)
		REFERENCES users(user_id)
		ON DELETE CASCADE
);

-- Per-question record of what the claimant actually typed, kept for admin
-- review so a human can see exactly which questions were missed and by how
-- much, instead of just the final percentage.
CREATE TABLE claim_answers(
    answer_id INT AUTO_INCREMENT PRIMARY KEY,
    claim_id INT NOT NULL,
    question_id INT NOT NULL,
    submitted_answer VARCHAR(255),
    is_correct BOOLEAN DEFAULT FALSE,
		CONSTRAINT fk_ca_claim
		FOREIGN KEY(claim_id)
		REFERENCES claims(claim_id)
		ON DELETE CASCADE,
		CONSTRAINT fk_ca_question
		FOREIGN KEY(question_id)
		REFERENCES verification_questions(question_id)
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