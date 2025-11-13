-- MySQL dump 10.13  Distrib 8.0.39, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: DevTrivia
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` (`id`, `name`) VALUES (4,'Java'),(3,'Python'),(2,'SQL'),(1,'Web Development');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum_posts`
--

DROP TABLE IF EXISTS `forum_posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forum_posts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `content` varchar(1000) NOT NULL,
  `post_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_post_user` (`user_id`),
  CONSTRAINT `fk_post_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_posts`
--

LOCK TABLES `forum_posts` WRITE;
/*!40000 ALTER TABLE `forum_posts` DISABLE KEYS */;
/*!40000 ALTER TABLE `forum_posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum_topics`
--

DROP TABLE IF EXISTS `forum_topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forum_topics` (
  `id` int DEFAULT NULL,
  `topic_name` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_topics`
--

LOCK TABLES `forum_topics` WRITE;
/*!40000 ALTER TABLE `forum_topics` DISABLE KEYS */;
/*!40000 ALTER TABLE `forum_topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `option_a` varchar(255) DEFAULT NULL,
  `option_b` varchar(255) DEFAULT NULL,
  `option_c` varchar(255) DEFAULT NULL,
  `option_d` varchar(255) DEFAULT NULL,
  `correct_answer` varchar(255) NOT NULL,
  `difficulty_level` tinyint DEFAULT NULL,
  `question_text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_question_category` (`category_id`),
  CONSTRAINT `fk_question_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE,
  CONSTRAINT `question_chk_1` CHECK ((`difficulty_level` between 0 and 2))
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` (`id`, `category_id`, `text`, `option_a`, `option_b`, `option_c`, `option_d`, `correct_answer`, `difficulty_level`, `question_text`) VALUES (1,1,'What does HTML stand for?','Hyperlinks and Text Markup Language','Hyper Text Markup Language','Home Tool Markup Language','Hyper Tool Markup Language','Hyper Text Markup Language',NULL,NULL),(2,1,'Which HTML tag is used to define an unordered list?','<ol>','<ul>','<li>','<list>','<ul>',NULL,NULL),(3,1,'What attribute is used to specify the URL in an <a> tag?','src','link','href','url','href',NULL,NULL),(4,1,'Which HTML5 element is used to embed video content?','<media>','<video>','<movie>','<embed>','<video>',NULL,NULL),(5,1,'What is the purpose of the <meta> tag in HTML?','To display metadata on the page','To link external stylesheets','To define metadata about the document','To create navigation menus','To define metadata about the document',NULL,NULL),(6,1,'What does CSS stand for?','Computer Style Sheets','Cascading Style Sheets','Creative Style Syntax','Colorful Style Sheets','Cascading Style Sheets',NULL,NULL),(7,1,'Which CSS property controls the stacking order of elements?','position','z-index','display','order','z-index',NULL,NULL),(8,1,'What is the default position value in CSS?','relative','absolute','fixed','static','static',NULL,NULL),(9,1,'Which selector targets all <p> elements inside a <div>?','div + p','div > p','div p','p div','div p',NULL,NULL),(10,1,'What does the !important declaration do in CSS?','Removes all styles from an element','Applies only in media queries','Gives the style highest priority','Applies only to hover states','Gives the style highest priority',NULL,NULL),(11,1,'What keyword is used to declare a constant in JavaScript?','var','let','const','define','const',NULL,NULL),(12,1,'What does NaN stand for?','Not a Node','Null and None','Not a Number','No Assigned Name','Not a Number',NULL,NULL),(13,1,'Which method is used to add an event listener to a DOM element?','addEvent()','onEvent()','attachEvent()','addEventListener()','addEventListener()',NULL,NULL),(14,1,'What does typeof null return in JavaScript?','\"null\"','\"object\"','\"undefined\"','\"boolean\"','\"object\"',NULL,NULL),(15,1,'What is the difference between == and ===?','== compares values and types','=== compares values only','== compares values only; === compares both','They are interchangeable','== compares values only; === compares both',NULL,NULL),(16,1,'What does the Fetch API replace in modern JavaScript?','XMLHttpRequest','JSON.parse()','WebSocket','setTimeout()','XMLHttpRequest',NULL,NULL),(17,1,'What tool is commonly used to measure website performance in Chrome?','Chrome Inspector','Lighthouse','Web Vitals','DevTools Console','Lighthouse',NULL,NULL),(18,1,'What does the acronym CORS stand for?','Cross-Origin Resource Sharing','Client-Origin Request System','Cascading Object Rendering Syntax','Content Optimization and Rendering Strategy','Cross-Origin Resource Sharing',NULL,NULL),(19,1,'Which HTTP status code means “Not Found”?','200','403','404','500','404',NULL,NULL),(20,1,'What is the purpose of a Content Delivery Network (CDN)?','To store user data','Deliver content faster via distributed servers','To manage cookies','To block malicious traffic','Deliver content faster via distributed servers',NULL,NULL),(21,1,'What does ARIA stand for in web accessibility?','Accessible Rich Internet Applications','Advanced Responsive Interface Architecture','Automated Rendering Interface API','Adaptive Resource Integration Access','Accessible Rich Internet Applications',NULL,NULL),(22,1,'Which HTML attribute provides alternative text for images?','title','alt','src','desc','alt',NULL,NULL),(23,1,'What is the recommended contrast ratio for text accessibility?','2:01','3:01','4.5:1','6:01','4.5:1',NULL,NULL),(24,1,'Which keyboard key is commonly used to navigate through focusable items?','Enter','Tab','Shift','Ctrl','Tab',NULL,NULL),(25,1,'Who is considered the inventor of the World Wide Web?','Bill Gates','Steve Jobs','Tim Berners-Lee','Vint Cerf','Tim Berners-Lee',NULL,NULL),(26,1,'What does the Content-Security-Policy HTTP header primarily protect against?','CSRF','XSS','SQL Injection','CORS issues','XSS',NULL,NULL),(27,1,'What is the main purpose of a Service Worker in a Progressive Web App?','Manipulate the DOM in background','Cache assets and enable offline support','Improve SEO performance','Speed up animations','Cache assets and enable offline support',NULL,NULL),(28,1,'In JavaScript, what does Object.freeze() do?','Locks object in memory','Prevents new properties and makes existing ones immutable','Converts object to a string','Prevents prototype inheritance','Prevents new properties and makes existing ones immutable',NULL,NULL),(29,1,'Which CSS feature allows you to detect the user’s preferred color scheme (e.g., dark mode)?','@theme','@query','@media (prefers-color-scheme)','@color-mode','@media (prefers-color-scheme)',NULL,NULL),(30,1,'Which of these JavaScript methods is asynchronous?','Array.prototype.forEach()','Array.prototype.map()','fetch()','JSON.parse()','fetch()',NULL,NULL),(31,1,'What is the maximum number of CSS selectors allowed in a single rule in IE9?','2048','4095','8192','Unlimited','4095',NULL,NULL),(32,1,'What’s the output of this JavaScript expression: [] + []?','NaN','0','[]','\"\" (empty string)','\"\" (empty string)',NULL,NULL),(33,1,'In HTML, what is the correct attribute to allow cross‑origin use of <script>?','crossorigin','allow-origin','access-control','cors','crossorigin',NULL,NULL),(34,1,'Which event is fired when a CSS transition completes?','onanimationend','ontransitioncomplete','transitionend','animationend','transitionend',NULL,NULL),(35,1,'Which JavaScript feature lets you pause function execution and resume later?','Promises','Generators','Arrow functions','Closures','Generators',NULL,NULL),(36,1,'Which HTML element is not allowed to be a direct child of <ul>?','<li>','<div>','<template>','<script>','<div>',NULL,NULL),(37,1,'Which HTTP status code indicates a successful response with no content?','200','204','304','202','204',NULL,NULL),(38,1,'What’s the output of: typeof null?','null','undefined','object','function','object',NULL,NULL),(39,1,'Which method is used to securely hash a password on the frontend?','MD5','SHA-1','bcrypt','None; hashing should be done server-side','None; hashing should be done server-side',NULL,NULL),(40,1,'In React, which hook is used to run side effects?','useState','useMemo','useCallback','useEffect','useEffect',NULL,NULL),(41,1,'Which CSS property is used to prevent flex items from shrinking?','flex-shrink: 1','flex-grow: 1','flex: none','flex-wrap: nowrap','flex: none',NULL,NULL),(42,1,'Which header is used in CORS preflight requests?','Access-Control-Allow-Origin','Access-Control-Request-Method','Access-Control-Allow-Headers','Origin','Access-Control-Request-Method',NULL,NULL),(43,1,'What’s the default display value for a <span> element?','block','inline-block','inline','none','inline',NULL,NULL),(44,1,'In Node.js, which module is used for cryptographic operations?','crypto','security','hashing','buffer','crypto',NULL,NULL),(45,1,'In the CSS box model, which area comes after padding?','Border','Margin','Content','Outline','Border',NULL,NULL),(46,1,'What is the purpose of the rel=\"noopener noreferrer\" attribute in anchor tags?','Improves SEO','Prevents access to window.opener','Enables CORS','Disables referrer spoofing','Prevents access to window.opener',NULL,NULL),(47,1,'What is the default HTTP method used when submitting an HTML form?','GET','POST','PUT','OPTIONS','GET',NULL,NULL),(48,1,'In JavaScript, how do you create a deep clone of an object (without external libraries)?','Object.assign({}, obj)','JSON.parse(JSON.stringify(obj))','Object.create(obj)','Object.copy(obj)','JSON.parse(JSON.stringify(obj))',NULL,NULL),(49,1,'Which of the following is not a valid JavaScript data type?','Symbol','Tuple','BigInt','Undefined','Tuple',NULL,NULL),(50,1,'In Webpack, what does the mode: \"production\" setting do?','Enables live reload','Minifies and optimizes output','Disables tree shaking','Increases build size','Minifies and optimizes output',NULL,NULL),(51,2,'Which SQL clause is used to filter records based on specified criteria?','FROM','SELECT','WHERE','GROUP BY','WHERE',NULL,NULL),(52,2,'What is the command to return only unique values in a column?','UNIQUE','DISTINCT','ONLY','DIFFERENT','DISTINCT',NULL,NULL),(53,2,'Which join returns all records from the left table, and the matched records from the right table?','INNER JOIN','RIGHT JOIN','FULL JOIN','LEFT JOIN','LEFT JOIN',NULL,NULL),(54,2,'Which aggregate function calculates the average of a set of values?','SUM()','AVG()','MEAN()','COUNT()','AVG()',NULL,NULL),(55,2,'What is the purpose of the HAVING clause?','To filter rows before aggregation','To filter column names','To filter groups after aggregation','To define a primary key','To filter groups after aggregation',NULL,NULL),(56,2,'Which type of command is CREATE TABLE?','DML (Data Manipulation Language)','DCL (Data Control Language)','DDL (Data Definition Language)','TCL (Transaction Control Language)','DDL (Data Definition Language)',NULL,NULL),(57,2,'Which operator is used for pattern matching in a query?','=','IN','LIKE','BETWEEN','LIKE',NULL,NULL),(58,2,'How do you delete all data in a table without deleting the table structure?','DROP TABLE','REMOVE ALL','DELETE ALL','TRUNCATE TABLE','TRUNCATE TABLE',NULL,NULL),(59,2,'Which constraint ensures that all values in a column are different?','FOREIGN KEY','NOT NULL','PRIMARY KEY','UNIQUE','UNIQUE',NULL,NULL),(60,2,'What does ACID stand for in the context of database transactions?','Access, Consistency, Integrity, Durability','Atomicity, Consistency, Isolation, Durability','Authentication, Concurrency, Isolation, Deletion','Atomicity, Con Control, Integrity, Data','Atomicity, Consistency, Isolation, Durability',NULL,NULL),(61,3,'Which keyword is used to define a function in Python?','func','define','def','function','def',NULL,NULL),(62,3,'What is the correct way to comment out multiple lines in Python?','// Comment','','\"\"\" Comment \"\"\"','# Comment','\"\"\" Comment \"\"\"',NULL,NULL),(63,3,'Which data structure is ordered, changeable, and allows duplicate members?','Tuple','Set','Dictionary','List','List',NULL,NULL),(64,3,'How do you check the type of a variable in Python?','typeof(var)','type(var)','get_type(var)','var.type','type(var)',NULL,NULL),(65,3,'What is the purpose of the __init__ method in a Python class?','To initialize module constants','To define a destructor','To serve as the class constructor','To create a static method','To serve as the class constructor',NULL,NULL),(66,3,'Which module is commonly used for making HTTP requests?','os','requests','json','sys','requests',NULL,NULL),(67,3,'What is a generator in Python?','A function that uses return','A function that uses the yield keyword','A class that generates random numbers','A built-in array','A function that uses the yield keyword',NULL,NULL),(68,3,'Which of these is an immutable data type in Python?','List','Dictionary','Set','Tuple','Tuple',NULL,NULL),(69,3,'What does PEP 8 primarily standardize?','Memory allocation','Package management','Python code style guidelines','Asynchronous programming','Python code style guidelines',NULL,NULL),(70,3,'What will be the value of x after x = 5 / 2?','2.5','2','3','Error','2.5',NULL,NULL),(71,4,'Which keyword is used to prevent a class from being subclassed?','static','abstract','final','void','final',NULL,NULL),(72,4,'Which data structure is used to implement a LIFO (Last-In, First-Out) stack?','Queue','List','Stack','Set','Stack',NULL,NULL),(73,4,'What is the entry point for execution in a standard Java application?','main()','init()','run()','start()','main()',NULL,NULL),(74,4,'Which concept allows an object to take on many forms (e.g., method overriding)?','Inheritance','Encapsulation','Abstraction','Polymorphism','Polymorphism',NULL,NULL),(75,4,'What does JVM stand for?','Java Variable Manager','Java Virtual Machine','Java Verified Module','Joint Venture Model','Java Virtual Machine',NULL,NULL),(76,4,'Which method is used to compare two strings for equality in Java?','s1 == s2','s1.equals(s2)','s1.compare(s2)','s1.isEqual(s2)','s1.equals(s2)',NULL,NULL),(77,4,'Which block is always executed regardless of whether an exception is thrown?','try','catch','throws','finally','finally',NULL,NULL),(78,4,'What is the default value of an instance variable of type int in Java?','1','null','0','Undefined','0',NULL,NULL),(79,4,'Which access modifier restricts access to the class itself and its subclasses?','public','private','protected','default','protected',NULL,NULL),(80,4,'What is the term for automatically managing and reclaiming memory no longer in use?','Memory Leakage','Garbage Collection','Object Disposal','Heap Management','Garbage Collection',NULL,NULL);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session`
--

DROP TABLE IF EXISTS `session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `score` int NOT NULL,
  `total_questions` int NOT NULL,
  `completion_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_session_user` (`user_id`),
  CONSTRAINT `fk_session_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session`
--

LOCK TABLES `session` WRITE;
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
/*!40000 ALTER TABLE `session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `security_answer_hash` varchar(255) DEFAULT NULL,
  `join_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `games_played` int DEFAULT '0',
  `total_correct` int DEFAULT '0',
  `is_admin` smallint DEFAULT NULL,
  `security_question` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `username`, `password_hash`, `email`, `security_answer_hash`, `join_date`, `games_played`, `total_correct`, `is_admin`, `security_question`) VALUES (1,'test','test1234','test@test.com','test','2025-11-08 21:10:17',0,0,0,NULL),(2,'lheath','$2a$10$AckpBd7fiB5B9xdCEi73BuFSUgWGIrplIdeAV3ztEmXrhjDZS/Jmi','heath@test.com','$2a$10$5V5JIBHendnaSPp.JTtQeORC0Mnej0p/I8iav2iEwigByyjjJhkP6','2025-11-11 05:06:45',0,0,1,'What is your favorite teacher\'s last name?'),(3,'admin','$2a$10$jvptuhzzyHYfc7MCgTX3c.tEk3g/MMd4eGCV7rS7cGM64OqOgL3li','admin@example.com',NULL,'2025-11-13 08:19:05',0,0,1,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'DevTrivia'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-12 20:41:03
