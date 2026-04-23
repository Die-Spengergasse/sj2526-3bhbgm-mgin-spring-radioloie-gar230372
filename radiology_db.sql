-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 23. Apr 2026 um 14:55
-- Server-Version: 10.4.32-MariaDB
-- PHP-Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `radiology_db`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `device`
--

CREATE TABLE `device` (
  `id` int(11) NOT NULL,
  `geräte_art` varchar(255) DEFAULT NULL,
  `raum_nr` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `device`
--

INSERT INTO `device` (`id`, `geräte_art`, `raum_nr`) VALUES
(1, 'Röntgen', 25),
(2, 'Ultraschall', 12),
(3, 'CT', 8),
(4, 'MRT', 13),
(5, 'EKG', 9),
(6, 'EEG', 7);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `patient`
--

CREATE TABLE `patient` (
  `id` int(11) NOT NULL,
  `birth` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `fristname` varchar(255) DEFAULT NULL,
  `gender` varchar(1) NOT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `svnr` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `patient`
--

INSERT INTO `patient` (`id`, `birth`, `name`, `fristname`, `gender`, `lastname`, `svnr`, `firstname`) VALUES
(1, '2026-04-25', NULL, NULL, '\0', NULL, '0', 'leo'),
(2, '2026-03-30', NULL, NULL, '\0', 'lehrach', '0', 'marie'),
(3, '2026-04-18', NULL, NULL, '\0', 'gartner', '0', 'isabel'),
(4, '2026-04-15', NULL, NULL, '\0', 'hanninger', '0', 'thomas'),
(5, '2026-04-09', NULL, NULL, 'f', 'gartner', '1122337788', 'hedwig'),
(6, '2026-04-05', NULL, NULL, 'm', 'gartner', '5544663377', 'wilfried'),
(7, '2000-04-19', NULL, NULL, 'f', 'Test', '654321789', 'Isabel');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `reservation`
--

CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `time` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `device_id` int(11) DEFAULT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `body_region` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `reservation`
--

INSERT INTO `reservation` (`id`, `time`, `date`, `device_id`, `patient_id`, `body_region`, `comment`) VALUES
(1, 'sfgfdsg', '2026-04-10', 3, 3, NULL, NULL),
(2, 'adfdsaf', '2026-04-17', 2, 2, NULL, NULL),
(3, 'adfdsaf', '2026-04-17', 1, 3, NULL, NULL),
(4, '12:50', '2026-04-09', 1, 2, NULL, NULL),
(5, '00:00', '2026-04-29', 1, 4, NULL, NULL),
(6, '8:00', '2026-04-13', 3, 5, NULL, NULL),
(7, '12:00', '2026-04-26', 5, 4, NULL, NULL),
(8, '20:00', '2026-04-11', 5, 2, NULL, 'none'),
(9, '10:30', '2026-05-10', 4, 6, 'extremities', 'none'),
(10, '23:00', '2026-04-25', 4, 7, 'abdomen', 'none'),
(11, '12:12', '2026-04-24', 2, 1, 'abdomen', '123'),
(12, '10:50', '2026-04-11', 1, 1, 'head', '121');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `device`
--
ALTER TABLE `device`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKcjc9b9oyt5lwt049mjoiqtb7r` (`device_id`),
  ADD KEY `FKrrjvkskqqxgliwmqgbl3ijc4n` (`patient_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `device`
--
ALTER TABLE `device`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT für Tabelle `patient`
--
ALTER TABLE `patient`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT für Tabelle `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `FKcjc9b9oyt5lwt049mjoiqtb7r` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`),
  ADD CONSTRAINT `FKrrjvkskqqxgliwmqgbl3ijc4n` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
