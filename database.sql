-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 28, 2025 at 03:27 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pam_ucp`
--

-- --------------------------------------------------------

--
-- Table structure for table `hewan`
--

CREATE TABLE `hewan` (
  `idHewan` int(11) NOT NULL,
  `namaHewan` varchar(100) NOT NULL,
  `tipePakan` varchar(50) NOT NULL,
  `populasi` int(11) NOT NULL,
  `zonaWilayah` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hewan`
--

INSERT INTO `hewan` (`idHewan`, `namaHewan`, `tipePakan`, `populasi`, `zonaWilayah`) VALUES
(2, 'Gajah', 'Herbivora', 5, 'Zona Asiatis'),
(4, 'Kanguru', 'Herbivora', 6, 'Zona Australis'),
(5, 'Burung Unta', 'Omnivora', 2, 'Zona Ethiopian'),
(6, 'Komodo', 'Karnivora', 3, 'Zona Asiatis'),
(7, 'Panda', 'Herbivora', 4, 'Zona Paleartik'),
(8, 'Buaya', 'Karnivora', 5, 'Zona Neotropik'),
(9, 'Burung Hantu', 'Karnivora', 6, 'Zona Paleartik'),
(10, 'Flamingo', 'Omnivora', 5, 'Zona Neotropik'),
(11, 'Kuda Nil', 'Herbivora', 3, 'Zona Ethiopian'),
(12, 'Cendrawasih', 'Omnivora', 4, 'Zona Australis'),
(13, 'Singa', 'Karnivora', 3, 'Zona Ethiopian'),
(15, 'Elang', 'Karnivora', 6, 'Zona Paleartik'),
(16, 'Tapir', 'Herbivora', 2, 'Zona Neotropik'),
(17, 'Lumba-lumba', 'Karnivora', 4, 'Zona Neotropik'),
(18, 'Rusa', 'Herbivora', 5, 'Zona Neartik'),
(19, 'Burung Kakak Tua', 'Omnivora', 4, 'Zona Australis'),
(20, 'Hiu', 'Karnivora', 2, 'Zona Neotropik');

-- --------------------------------------------------------

--
-- Table structure for table `kandang`
--

CREATE TABLE `kandang` (
  `idKandang` int(11) NOT NULL,
  `idHewan` int(11) NOT NULL,
  `kapasitas` int(11) NOT NULL,
  `lokasi` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kandang`
--

INSERT INTO `kandang` (`idKandang`, `idHewan`, `kapasitas`, `lokasi`) VALUES
(4, 4, 6, '12313qwdqdq'),
(5, 5, 2, 'Zona B'),
(6, 6, 3, 'Zona A'),
(7, 7, 4, 'Zona B'),
(8, 8, 5, 'Zona C'),
(9, 9, 6, 'Zona A'),
(10, 10, 5, 'Zona C'),
(11, 11, 3, 'Zona D'),
(12, 12, 4, 'Zona C'),
(13, 13, 3, 'Zona A'),
(15, 15, 6, 'Zona C'),
(16, 16, 2, 'Zona B'),
(17, 17, 4, 'Zona C'),
(18, 18, 5, 'Zona A'),
(19, 19, 4, 'Zona B'),
(20, 20, 2, 'Zona C');

-- --------------------------------------------------------

--
-- Table structure for table `monitoring`
--

CREATE TABLE `monitoring` (
  `idMonitoring` int(11) NOT NULL,
  `idPetugas` int(11) NOT NULL,
  `idKandang` int(11) NOT NULL,
  `tanggalMonitoring` datetime(6) NOT NULL,
  `hewanSakit` int(11) NOT NULL,
  `hewanSehat` int(11) NOT NULL,
  `status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `monitoring`
--

INSERT INTO `monitoring` (`idMonitoring`, `idPetugas`, `idKandang`, `tanggalMonitoring`, `hewanSakit`, `hewanSehat`, `status`) VALUES
(3, 2, 12, '2025-01-26 12:29:54.749155', 2, 2, 'Kritis'),
(4, 4, 5, '2025-01-28 11:45:00.000000', 7, 43, 'Waspada'),
(88, 2, 7, '2025-01-26 11:31:45.118049', 0, 4, 'Aman'),
(89, 2, 5, '2025-01-28 20:36:48.973106', 0, 2, 'Aman'),
(90, 3, 6, '2025-01-28 21:02:26.531597', 0, 3, 'Aman');

-- --------------------------------------------------------

--
-- Table structure for table `petugas`
--

CREATE TABLE `petugas` (
  `idPetugas` int(11) NOT NULL,
  `namaPetugas` varchar(100) NOT NULL,
  `jabatan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `petugas`
--

INSERT INTO `petugas` (`idPetugas`, `namaPetugas`, `jabatan`) VALUES
(1, 'Ahma', 'Dokter Hewan'),
(2, 'Budi', 'Dokter Hewan'),
(3, 'test', 'Kurator'),
(4, 'Dewi', 'Keeper'),
(5, 'Eka', 'Dokter Hewan'),
(6, 'Fajar', 'Kurator'),
(7, 'Gita', 'Keeper'),
(8, 'Hadi', 'Dokter Hewan'),
(9, 'Indah', 'Kurator'),
(10, 'Joko', 'Keeper'),
(13, 'a', 'Keeper'),
(14, 'LingGrani', 'Kurator');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `hewan`
--
ALTER TABLE `hewan`
  ADD PRIMARY KEY (`idHewan`);

--
-- Indexes for table `kandang`
--
ALTER TABLE `kandang`
  ADD PRIMARY KEY (`idKandang`),
  ADD KEY `kandang_ibfk_1` (`idHewan`);

--
-- Indexes for table `monitoring`
--
ALTER TABLE `monitoring`
  ADD PRIMARY KEY (`idMonitoring`),
  ADD KEY `monitoring_ibfk_1` (`idPetugas`),
  ADD KEY `monitoring_ibfk_2` (`idKandang`);

--
-- Indexes for table `petugas`
--
ALTER TABLE `petugas`
  ADD PRIMARY KEY (`idPetugas`),
  ADD UNIQUE KEY `namaPetugas` (`namaPetugas`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `hewan`
--
ALTER TABLE `hewan`
  MODIFY `idHewan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=104;

--
-- AUTO_INCREMENT for table `kandang`
--
ALTER TABLE `kandang`
  MODIFY `idKandang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=107;

--
-- AUTO_INCREMENT for table `monitoring`
--
ALTER TABLE `monitoring`
  MODIFY `idMonitoring` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=92;

--
-- AUTO_INCREMENT for table `petugas`
--
ALTER TABLE `petugas`
  MODIFY `idPetugas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `kandang`
--
ALTER TABLE `kandang`
  ADD CONSTRAINT `kandang_ibfk_1` FOREIGN KEY (`idHewan`) REFERENCES `hewan` (`idHewan`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `monitoring`
--
ALTER TABLE `monitoring`
  ADD CONSTRAINT `monitoring_ibfk_1` FOREIGN KEY (`idPetugas`) REFERENCES `petugas` (`idPetugas`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `monitoring_ibfk_2` FOREIGN KEY (`idKandang`) REFERENCES `kandang` (`idKandang`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
