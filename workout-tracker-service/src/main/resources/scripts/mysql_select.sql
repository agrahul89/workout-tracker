SELECT `user`.`id`,
    `user`.`firstname`,
    `user`.`lastname`,
    `user`.`midname`,
    `user`.`email`,
    `user`.`password`,
    `user`.`created`,
    `user`.`updated`
FROM `iiht`.`user`;

SELECT `category`.`id`,
    `category`.`userid`,
    `category`.`category`,
    `category`.`created`,
    `category`.`updated`
FROM `iiht`.`category`;

SELECT `workout`.`id`,
    `workout`.`userid`,
    `workout`.`categoryid`,
    `workout`.`title`,
    `workout`.`notes`,
    `workout`.`burnrate`,
    `workout`.`start`,
    `workout`.`end`,
    `workout`.`created`,
    `workout`.`updated`,
    `workout`.`deleted`
FROM `iiht`.`workout`;
