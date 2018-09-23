UPDATE `iiht`.`user`
SET
`id` = <{id: }>,
`firstname` = <{firstname: }>,
`lastname` = <{lastname: }>,
`midname` = <{midname: }>,
`email` = <{email: }>,
`password` = <{password: }>,
`created` = <{created: CURRENT_TIMESTAMP}>,
`updated` = <{updated: 0000-00-00 00:00:00}>
WHERE `id` = <{expr}>;

UPDATE `iiht`.`category`
SET
`id` = <{id: }>,
`userid` = <{userid: }>,
`category` = <{category: }>,
`created` = <{created: CURRENT_TIMESTAMP}>,
`updated` = <{updated: 0000-00-00 00:00:00}>
WHERE `id` = <{expr}>;

UPDATE `iiht`.`workout`
SET
`id` = <{id: }>,
`userid` = <{userid: }>,
`categoryid` = <{categoryid: }>,
`title` = <{title: }>,
`notes` = <{notes: }>,
`burnrate` = <{burnrate: 2}>,
`start` = <{start: }>,
`end` = <{end: }>,
`created` = <{created: CURRENT_TIMESTAMP}>,
`updated` = <{updated: 0000-00-00 00:00:00}>
`deleted` = <{deleted: NULL}>
WHERE `id` = <{expr}>;
