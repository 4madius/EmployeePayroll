-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Generation Time: Jan 08, 2026 at 10:33 PM
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
-- Database: `payroll_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL,
  `basic_salary` decimal(10,2) DEFAULT 0.00,
  `allowance` decimal(10,2) DEFAULT 0.00,
  `hourly_rate` decimal(10,2) DEFAULT 0.00,
  `hours_worked` int(11) DEFAULT 0,
  `contract_amount` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`id`, `name`, `type`, `basic_salary`, `allowance`, `hourly_rate`, `hours_worked`, `contract_amount`) VALUES
('A001', 'Ahmad bin Abdullah', 'Full Time', 4500.00, 500.00, 0.00, 0, 0.00),
('A002', 'Siti Sarah binti Rahman', 'Full Time', 3200.00, 300.00, 0.00, 0, 0.00),
('A003', 'Chong Wei Hong', 'Part Time', 0.00, 0.00, 50.00, 12, 0.00),
('A004', 'Ramasamy a/l Krishnan', 'Part Time', 0.00, 0.00, 20.00, 45, 0.00),
('A005', 'Nurul Izzah binti Anwar', 'Contract', 0.00, 0.00, 0.00, 0, 5000.00),
('T001', 'nabil', 'Full Time', 1500.00, 200.00, 0.00, 0, 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `payslips`
--

CREATE TABLE `payslips` (
  `payslip_id` int(11) NOT NULL,
  `employee_id` varchar(10) DEFAULT NULL,
  `payment_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `gross_salary` decimal(10,2) DEFAULT NULL,
  `tax_amount` decimal(10,2) DEFAULT NULL,
  `net_salary` decimal(10,2) DEFAULT NULL,
  `hours_worked` int(11) DEFAULT 0,
  `hourly_rate` decimal(10,2) DEFAULT 0.00,
  `allowance` decimal(10,2) DEFAULT 0.00,
  `contract_amount` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payslips`
--

INSERT INTO `payslips` (`payslip_id`, `employee_id`, `payment_date`, `gross_salary`, `tax_amount`, `net_salary`, `hours_worked`, `hourly_rate`, `allowance`, `contract_amount`) VALUES
(1, 'A001', '2026-01-07 19:52:21', 5000.00, 250.00, 4750.00, 0, 0.00, 0.00, 0.00),
(2, 'A002', '2026-01-07 19:52:21', 3500.00, 175.00, 3325.00, 0, 0.00, 0.00, 0.00),
(3, 'A003', '2026-01-07 19:52:21', 600.00, 30.00, 570.00, 0, 0.00, 0.00, 0.00),
(4, 'A004', '2026-01-07 19:52:21', 900.00, 45.00, 855.00, 0, 0.00, 0.00, 0.00),
(5, 'A005', '2026-01-07 19:52:21', 5000.00, 250.00, 4750.00, 0, 0.00, 0.00, 0.00),
(6, 'T001', '2026-01-07 19:52:21', 1700.00, 85.00, 1615.00, 0, 0.00, 0.00, 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `db_key` int(11) NOT NULL,
  `staff_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`db_key`, `staff_id`, `name`, `password`) VALUES
(1, 'HR01', 'Siti Sarah binti Rahman', 'admin123'),
(2, 'AD01', 'Ahmad Albab bin Bakar', 'admin123'),
(3, 'HR02', 'Mei Ling', 'admin123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payslips`
--
ALTER TABLE `payslips`
  ADD PRIMARY KEY (`payslip_id`),
  ADD KEY `employee_id` (`employee_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`db_key`),
  ADD UNIQUE KEY `staff_id` (`staff_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `payslips`
--
ALTER TABLE `payslips`
  MODIFY `payslip_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `db_key` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `payslips`
--
ALTER TABLE `payslips`
  ADD CONSTRAINT `payslips_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
