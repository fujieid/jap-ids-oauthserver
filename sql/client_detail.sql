-- Host: 127.0.0.1    Database: jap_ids_db
-- ------------------------------------------------------
-- Server version	8.0.21

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
-- Table structure for table `client_detail`
--

DROP TABLE IF EXISTS `client_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client_detail` (
  `id` varchar(50) NOT NULL,
  `app_name` varchar(50) NOT NULL,
  `client_id` varchar(50) NOT NULL,
  `client_secret` varchar(50) NOT NULL,
  `site_domain` varchar(50) DEFAULT NULL,
  `redirect_uri` varchar(50) NOT NULL,
  `logout_redirect_uri` varchar(50) DEFAULT NULL,
  `logo` varchar(50) DEFAULT NULL,
  `available` tinyint(1) DEFAULT '1',
  `description` text,
  `scopes` varchar(100) DEFAULT NULL,
  `grant_types` varchar(100) DEFAULT NULL,
  `response_types` varchar(100) DEFAULT NULL,
  `code_expires_in` mediumtext,
  `id_token_expires_in` mediumtext,
  `access_token_expires_in` mediumtext,
  `refresh_token_expires_in` mediumtext,
  `auto_approve` tinyint(1) DEFAULT NULL,
  `enable_pkce` tinyint(1) DEFAULT NULL,
  `code_challenge_method` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_detail`
--

LOCK TABLES `client_detail` WRITE;
/*!40000 ALTER TABLE `client_detail` DISABLE KEYS */;
INSERT INTO `client_detail` (`id`, `app_name`, `client_id`, `client_secret`, `site_domain`, `redirect_uri`, `logout_redirect_uri`, `logo`, `available`, `description`, `scopes`, `grant_types`, `response_types`, `code_expires_in`, `id_token_expires_in`, `access_token_expires_in`, `refresh_token_expires_in`, `auto_approve`, `enable_pkce`, `code_challenge_method`) VALUES ('20ab688b719d46229143dd9e558b2786','test1','zehtufcp4v3zs8nzs2ldp7hkd22y00t9','8dnf2r7diltoj1nx98iix0ydbayldq3c1wg00006',NULL,'http://localhost:8888',NULL,NULL,1,NULL,'read write openid profile email phone address','authorization_code implicit password client_credentials refresh_token token','code token id_token none',NULL,NULL,NULL,NULL,NULL,1,'S256'),('3c87d931cbcc4dcb861dda5c71d4cd34','test2','l4qg58m9l4qqxyoqkjbhcghrb9g4mw8a','3c3x7pi304xp2x150edipr5nogxhcdok07j760w2',NULL,'http://localhost:8888',NULL,NULL,1,NULL,'read write openid profile email phone address','authorization_code implicit password client_credentials refresh_token token','code token id_token none',NULL,NULL,NULL,NULL,NULL,1,'plain'),('47d6e02412ee4559b1cd6da3bd26043c','test3','vysi6cywq9b0o9c2x6q4qrfzfdljhod9','my1i30fgis9l0lxw7kpzxt5sp50ivxyedxgal144',NULL,'http://localhost:8888',NULL,NULL,1,NULL,'read write openid profile email phone address','authorization_code implicit password client_credentials refresh_token token','code token id_token none',NULL,NULL,NULL,NULL,NULL,1,NULL);
/*!40000 ALTER TABLE `client_detail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

