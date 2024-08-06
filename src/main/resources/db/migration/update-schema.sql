ALTER TABLE ted.advertisement
DROP
FOREIGN KEY fk_advertisement_user1;

ALTER TABLE ted.article
DROP
FOREIGN KEY fk_article_user1;

ALTER TABLE ted.comment
DROP
FOREIGN KEY fk_comment_user1;

ALTER TABLE ted.connection
DROP
FOREIGN KEY fk_connection_user2;

ALTER TABLE ted.`like`
DROP
FOREIGN KEY fk_like_user1;

ALTER TABLE ted.notification
DROP
FOREIGN KEY fk_notification_user2;

ALTER TABLE ted.profile
DROP
FOREIGN KEY fk_profile_user1;

ALTER TABLE ted.request
DROP
FOREIGN KEY fk_request_user1;

ALTER TABLE article
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE article
    MODIFY created_by VARCHAR (60) NOT NULL;

ALTER TABLE comment
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE comment
    MODIFY created_by VARCHAR (60) NOT NULL;

ALTER TABLE `like`
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE `like`
    ADD from_email VARCHAR(60) NULL;

ALTER TABLE `like`
    MODIFY created_by VARCHAR (60) NOT NULL;

ALTER TABLE profile
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE profile
    MODIFY created_by VARCHAR (60) NOT NULL;

ALTER TABLE request
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE request
    ADD from_email VARCHAR(60) NULL;

ALTER TABLE request
    ADD to_email VARCHAR(60) NULL;

ALTER TABLE request
    MODIFY created_by VARCHAR (60) NOT NULL;

ALTER TABLE connection
    ADD friend_email VARCHAR(60) NULL;

ALTER TABLE connection
    ADD user_email VARCHAR(60) NULL;

ALTER TABLE connection
    MODIFY friend_email VARCHAR (60) NOT NULL;

ALTER TABLE notification
    ADD friend_email VARCHAR(60) NULL;

ALTER TABLE notification
    ADD user_email VARCHAR(60) NULL;

ALTER TABLE notification
    MODIFY friend_email VARCHAR (60) NOT NULL;

ALTER TABLE `like`
    MODIFY from_email VARCHAR (60) NOT NULL;

ALTER TABLE request
    MODIFY from_email VARCHAR (60) NOT NULL;

ALTER TABLE request
    MODIFY to_email VARCHAR (60) NOT NULL;

ALTER TABLE connection
    MODIFY user_email VARCHAR (60) NOT NULL;

ALTER TABLE notification
    MODIFY user_email VARCHAR (60) NOT NULL;

ALTER TABLE article
    ADD CONSTRAINT FK_ARTICLE_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE connection
    ADD CONSTRAINT FK_CONNECTION_ON_FRIEND_EMAIL FOREIGN KEY (friend_email) REFERENCES user (email);

ALTER TABLE connection
    ADD CONSTRAINT FK_CONNECTION_ON_USER_EMAIL FOREIGN KEY (user_email) REFERENCES user (email);

ALTER TABLE `like`
    ADD CONSTRAINT FK_LIKE_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE `like`
    ADD CONSTRAINT FK_LIKE_ON_FROM_EMAIL FOREIGN KEY (from_email) REFERENCES user (email);

ALTER TABLE notification
    ADD CONSTRAINT FK_NOTIFICATION_ON_FRIEND_EMAIL FOREIGN KEY (friend_email) REFERENCES user (email);

ALTER TABLE notification
    ADD CONSTRAINT FK_NOTIFICATION_ON_USER_EMAIL FOREIGN KEY (user_email) REFERENCES user (email);

ALTER TABLE profile
    ADD CONSTRAINT FK_PROFILE_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE request
    ADD CONSTRAINT FK_REQUEST_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE request
    ADD CONSTRAINT FK_REQUEST_ON_FROM_EMAIL FOREIGN KEY (from_email) REFERENCES user (email);

ALTER TABLE request
    ADD CONSTRAINT FK_REQUEST_ON_TO_EMAIL FOREIGN KEY (to_email) REFERENCES user (email);

ALTER TABLE ted.user_role
DROP
PRIMARY KEY;

ALTER TABLE ted.advertisement
DROP
COLUMN createdBy;

ALTER TABLE ted.article
DROP
COLUMN createdBy;

ALTER TABLE ted.comment
DROP
COLUMN createdBy;

ALTER TABLE ted.`like`
DROP
COLUMN createdBy;

ALTER TABLE ted.profile
DROP
COLUMN createdBy;

ALTER TABLE ted.request
DROP
COLUMN createdBy;

ALTER TABLE ted.notification
DROP
COLUMN friend;

ALTER TABLE ted.connection
DROP
COLUMN user2;

ALTER TABLE article
    MODIFY multimedia_path VARCHAR (255);

ALTER TABLE user
    MODIFY password VARCHAR (255);

ALTER TABLE user
    MODIFY photo_path VARCHAR (255);
ALTER TABLE advertisement
    DROP FOREIGN KEY fk_advertisement_user1;

ALTER TABLE article
    DROP FOREIGN KEY fk_article_user1;

ALTER TABLE comment
    DROP FOREIGN KEY fk_comment_user1;

ALTER TABLE `like`
    DROP FOREIGN KEY fk_like_user1;

ALTER TABLE profile
    DROP FOREIGN KEY fk_profile_user1;

ALTER TABLE request
    DROP FOREIGN KEY fk_request_user1;

ALTER TABLE advertisement
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE advertisement
    MODIFY created_by VARCHAR(60) NOT NULL;

ALTER TABLE article
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE article
    MODIFY created_by VARCHAR(60) NOT NULL;

ALTER TABLE comment
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE comment
    MODIFY created_by VARCHAR(60) NOT NULL;

ALTER TABLE `like`
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE `like`
    MODIFY created_by VARCHAR(60) NOT NULL;

ALTER TABLE profile
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE profile
    MODIFY created_by VARCHAR(60) NOT NULL;

ALTER TABLE request
    ADD created_by VARCHAR(60) NULL;

ALTER TABLE request
    MODIFY created_by VARCHAR(60) NOT NULL;

ALTER TABLE advertisement
    ADD CONSTRAINT FK_ADVERTISEMENT_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE article
    ADD CONSTRAINT FK_ARTICLE_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE `like`
    ADD CONSTRAINT FK_LIKE_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE profile
    ADD CONSTRAINT FK_PROFILE_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE request
    ADD CONSTRAINT FK_REQUEST_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES user (email);

ALTER TABLE advertisement
    DROP COLUMN createdBy;

ALTER TABLE article
    DROP COLUMN createdBy;

ALTER TABLE comment
    DROP COLUMN createdBy;

ALTER TABLE `like`
    DROP COLUMN createdBy;

ALTER TABLE profile
    DROP COLUMN createdBy;

ALTER TABLE request
    DROP COLUMN createdBy;