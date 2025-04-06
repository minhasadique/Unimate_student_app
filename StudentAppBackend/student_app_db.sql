-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 06, 2025 at 05:22 PM
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
-- Database: `student_app_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `id` int(11) NOT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`id`, `group_name`, `created_at`) VALUES
(2, 'AI Enthusiasts', '2025-02-25 14:06:47'),
(3, 'Ethical Hackers Hub', '2025-03-13 09:04:57'),
(4, 'Deep Learning Club', '2025-03-27 10:11:27'),
(5, 'Bitcoin Miners', '2025-03-27 10:11:39'),
(6, 'Into the Cryptography', '2025-03-27 10:12:23'),
(7, 'Unimate Project', '2025-03-28 03:55:56');

-- --------------------------------------------------------

--
-- Table structure for table `group_messages`
--

CREATE TABLE `group_messages` (
  `id` int(11) NOT NULL,
  `group_id` int(11) DEFAULT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `group_messages`
--

INSERT INTO `group_messages` (`id`, `group_id`, `sender_id`, `message`, `timestamp`) VALUES
(3, 2, 7, 'helloo', '2025-03-15 11:47:27'),
(4, 2, 7, 'helloo ai enthusisastss', '2025-03-15 11:48:26'),
(5, 3, 7, 'Helloo Hacking loverss', '2025-03-15 14:05:12'),
(6, 3, 8, 'Hello Hackerss', '2025-03-27 11:52:30'),
(7, 3, 8, 'Anyone here experienced hackers?', '2025-03-27 11:52:44');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `message` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `sender_id`, `receiver_id`, `message`, `timestamp`) VALUES
(1, 1, 2, 'Hello, testing!', '2025-02-19 09:28:18'),
(2, 1, 2, 'Hello, Who is it!', '2025-02-19 09:28:58'),
(3, 1, 2, 'Hello, Who is it!', '2025-02-19 09:23:00'),
(4, 2, 2, 'Helloooo woorlldd', '2025-02-19 05:19:13'),
(5, 1, 2, 'Hello, Who is it!', '2025-02-19 09:23:00'),
(6, 2, 2, 'hellooo', '2025-02-19 09:41:34'),
(7, 2, 2, 'heyy meznaaaaa', '2025-02-19 09:41:51'),
(8, 2, 2, 'Heyyy', '2025-02-19 09:43:39'),
(9, 2, 2, 'Hellooooooooo', '2025-02-19 10:15:32'),
(10, 2, 2, 'Heyy Meznaaa Sadique', '2025-02-19 13:29:42'),
(11, 1, 2, 'heyy minnaaa', '2025-02-19 14:17:10'),
(12, 3, 2, 'Helloo Sirr', '2025-02-19 14:17:45'),
(13, 5, 2, 'heyy', '2025-02-19 14:22:20'),
(14, 3, 2, 'Helloo Mr Yazid', '2025-02-20 01:22:23'),
(15, 2, 2, 'Meznaa  are you admin', '2025-02-20 01:23:46'),
(16, 1, 2, 'Hello!', '2025-02-22 15:22:19'),
(17, 1, 3, 'Heyyooo!', '2025-02-22 15:23:28'),
(18, 1, 3, 'heyyy', '2025-02-23 03:55:50'),
(19, 1, 3, 'hello yaazid', '2025-02-23 03:56:12'),
(20, 1, 2, 'heyyy meznaaa', '2025-02-23 03:57:01'),
(21, 1, 1, 'heyyy minnaa', '2025-02-23 03:57:56'),
(22, 1, 5, 'helloo zehnaaa', '2025-02-23 03:58:39'),
(23, 5, 1, 'heyy minnaa...me zehnaaa', '2025-02-23 04:06:00'),
(24, 5, 1, 'helloo', '2025-02-23 05:49:26'),
(25, 1, 2, 'Heyy Meznaaa', '2025-02-23 10:05:46'),
(26, 1, 3, 'hello sirr', '2025-02-23 11:20:16'),
(27, 1, 2, 'heyy', '2025-02-23 11:23:16'),
(28, 1, 3, 'helloo', '2025-02-23 11:27:43'),
(29, 1, 5, 'helloo zehna....Do you do web designing?', '2025-02-23 11:38:36'),
(30, 5, 1, 'Yup, I\'m having few years of experience in it', '2025-02-23 14:55:23'),
(31, 5, 3, 'Helloo sirr', '2025-02-23 14:56:04'),
(32, 6, 1, 'Helloo Minha Sadique', '2025-02-25 03:59:42'),
(33, 7, 1, 'Helloo Minha', '2025-02-25 04:31:30'),
(34, 11, 7, 'Heyy Admin', '2025-02-27 03:03:14'),
(35, 7, 11, 'Helloo Danii...May I help u..', '2025-03-15 06:14:52'),
(36, 15, 7, 'Hello admin', '2025-03-27 06:01:02'),
(37, 15, 7, 'I had a serious doubt in the applying of scholarships for the students with low annual income', '2025-03-27 06:01:49'),
(38, 15, 7, 'Will you be able to help me with that or refer someone', '2025-03-27 06:02:13'),
(39, 7, 15, 'Sure', '2025-03-27 06:02:46'),
(40, 17, 1, 'heyy', '2025-03-27 23:26:33');

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE `posts` (
  `id` int(11) NOT NULL,
  `postAuthor1` varchar(255) NOT NULL,
  `postText1` text NOT NULL,
  `postImage1` longblob DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`id`, `postAuthor1`, `postText1`, `postImage1`, `created_at`, `user_id`) VALUES
(3, 'Minha Sadique', 'No colleges on tomorrow', 0x414243202831292e6a7067, '2025-02-14 15:18:54', 2),
(6, 'Micheal Jackson', 'Will be on PWC Tomorrow evening', NULL, '2025-02-16 14:36:05', 1),
(7, 'Calicut University', 'The exams of the sixth semester students will be conducted after Eid Celebrations', NULL, '2025-02-16 14:39:10', 1),
(8, 'Providence Womens College', 'In the Calicut University Bzone Competitions, Amongs all the Colleges Our college has been placed at fourth position ✨🎉And first among the Womens Colleges.Congrats to all the students who have worked hard and achived success. Cheers to the whole team and wishing all of you success.', NULL, '2025-02-16 15:23:13', 1),
(9, 'Calicut University', 'New timetable for the sixth semester has published and available on the link below \n🔗https://pareekshabhavan.uoc.ac.in/index.php/examination/timetable', NULL, '2025-02-18 06:46:36', 1),
(10, 'Haniya Abdulla', 'The Success Party 🎉 of our ITFEST Eprayan\'25 will be held on February 20th after completing this academic years Internal Examinations at 1:30 pm at the Third Year Classrooms...All are Welcome', NULL, '2025-02-18 07:38:21', 1),
(11, 'Department of Computer Science', 'project', NULL, '2025-02-25 08:58:16', 1),
(12, 'Calicut University', 'Considering the CUET-PG exams, the sixth-semester (link unavailable) (CBCSS-UG) Regular/Supplementary/Improvement April-2025 (2019-2022 admissions) theory exams have been rescheduled from 03.04.2025 to 10.04.2025. The practical/viva exams are to be conducted on the scheduled dates from March 12 to 28', NULL, '2025-02-27 18:42:42', 1),
(13, 'Admin', 'New Feature Update!...Search your friends and connect with them based on their interests', NULL, '2025-02-27 18:50:04', 1),
(14, 'Scholarship Community', '📢 Exciting news! The National Merit Scholarship 2025 applications are now open for undergraduate students. Eligible students can receive up to 5,000 based on academic performance and financial need. Apply before March 15, 2025, through the official portal. Don\'t miss this chance to fund your education', NULL, '2025-03-05 06:55:17', 1),
(16, 'Tech Careers India', '🚀 Hiring Alert! Infosys is looking for freshers with Java and Python skills for their Software Engineer role. This is a great opportunity for 2024 pass-outs to kickstart their IT career. Apply before February 28, 2025, through the Infosys careers page. Good luck to all applicants!', NULL, '2025-03-05 06:56:12', 1),
(18, 'Student Support Center', '🎓 Great news for students! The XYZ International Scholarship 2025 is now open for applications. This fully funded program supports outstanding students pursuing STEM degrees. The deadline is March 30, 2025. Apply now and take a step toward your dream career!', NULL, '2025-03-26 17:37:52', 1),
(20, 'University', 'exam are postponed', NULL, '2025-03-28 04:01:06', 1),
(21, 'University', 'exam are postponed', NULL, '2025-03-28 04:01:06', 1);

-- --------------------------------------------------------

--
-- Table structure for table `private_messages`
--
-- Error reading structure for table student_app_db.private_messages: #1932 - Table &#039;student_app_db.private_messages&#039; doesn&#039;t exist in engine
-- Error reading data for table student_app_db.private_messages: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `student_app_db`.`private_messages`&#039; at line 1

-- --------------------------------------------------------

--
-- Table structure for table `startups`
--

CREATE TABLE `startups` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `author` varchar(100) NOT NULL,
  `roles` text NOT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `startups`
--

INSERT INTO `startups` (`id`, `title`, `description`, `author`, `roles`, `contact`, `created_at`) VALUES
(1, 'New Startup', 'It\'s an innovative concept of the day of the world 🌎 and the same time as well as the best for the next class of the day of the year 🎉 and the same time as discussed please see my resume to you too dear sir', 'Minha Sadique', 'Web Developer, Digital Marketer', '0', '2025-02-13 18:02:01'),
(2, 'community hub', 'where each community have a chance to grow', 'minha', 'student', '0', '2025-02-14 06:03:26'),
(3, 'Unimate App', 'It\'s an innovative concept of the day of the world and the same time as well as the best for the next class of the day of the day', 'Haniya Minha', 'Web Developer', '0', '2025-02-14 08:23:40'),
(4, 'NEW STARTUP', 'This is the startup that we have been planning since our childhood and we wnted to bring it to life with the help of my fellow unimates.', 'Micheal Jackson', 'Web Developer', '0', '2025-02-16 13:46:53'),
(5, 'HavenTrace', 'Missing Children Identification WebApp. Its an app implemented for overcoming scenarios by Adding Deep Learning into it.', 'Lana Nida Meha Nourin', 'Invester, Digital Marketer', '0', '2025-02-19 09:07:09'),
(11, 'startup', 'Binge Study – Your Ultimate Exam Companion\n\nBinge Study is the perfect app for students who need to master their entire syllabus efficiently before exams. Whether you\'re short on time or looking for a structured way to revise, Binge Study has got you covered.\n🤝 Group Study & Collaboration – Join or create study groups to discuss topics, share notes, and help each other succeed.\n\nWith Binge Study, last-minute exam preparation becomes stress-free and effective. Get ready to binge-study your way to success!', 'shilka', 'trainee', '123456', '2025-02-25 09:08:33');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `role` enum('student','staff') NOT NULL,
  `busPassImagePath` text DEFAULT NULL,
  `otp_code` varchar(10) DEFAULT NULL,
  `otp_expiry` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `created_at`, `role`, `busPassImagePath`, `otp_code`, `otp_expiry`) VALUES
(1, 'Minha Sadique', 'minhasadique424@gmail.com', '$2y$10$78RdqhRKt9dysuUuh7htSekGiLFbTuQz/9GPqS2nrJoMq1yzt1z7y', '2025-02-13 19:15:04', 'student', NULL, NULL, 0),
(2, 'Mezna Sadique', 'mezna@gmail.com', '$2y$10$Le4BtCJz4Gz.UFsDqo.FN.Hw0WZj8EpsjXBEkvkSOoTz0XXFI0ACG', '2025-02-13 19:48:33', 'staff', NULL, NULL, 0),
(3, 'Yazid Mohmd', 'yazidmohd@gmail.com', '$2y$10$TAszQZG2GReGOfKiSPr33O.NTCaiq0OkT9/qvDF4INXwzeZaPI6EO', '2025-02-14 06:55:03', 'staff', NULL, NULL, 0),
(4, 'Minha', 'minhasadique4242@gmail.com', '$2y$10$QGuUunWIxD/XYPTVTV/lT.bvbM3ecutQ7A7n5tK/VHLtTaAojxf3y', '2025-02-14 08:20:26', 'student', NULL, NULL, 0),
(5, 'Zehna Sadique', 'zehnasadique@gmail.com', '$2y$10$wwOr2POEuHLPJH6kJfw0u.PxdwmDmahTLGmSKpGrHhy63tBKEayFO', '2025-02-19 18:50:48', 'staff', NULL, NULL, 0),
(6, 'Haniya Abdulla', 'haniyaabdullamm@gmail.com', '$2y$10$C7R8AVW7mHnznemfZWRwpuUkIptR1qoNAZiw3W8ZQp75xaNf5c4am', '2025-02-25 07:05:51', 'student', NULL, NULL, 0),
(7, 'Unimate Admin', 'admin@gmail.com', '$2y$10$4Uq1up13UK3JUoX0oBtdfu5rbEuUD6dt3VDPz7unnz/ew7p3EecQu', '2025-02-25 08:33:35', 'staff', NULL, NULL, 0),
(8, 'shilka', 'shilkamv@gmail.com', '$2y$10$II/gnJBDM8YVihPY8H1h7O0LV.3DUss9sbv9diVl5CNHrRVSdLBZy', '2025-02-25 08:57:18', 'staff', NULL, NULL, 0),
(9, '12234', 'hjhgg', '$2y$10$saOYp4RhdDRxDYS9Zk9DcemO6mNUsosS8OjBT4aNm4QyMR8Z6sASu', '2025-02-25 09:14:00', 'staff', NULL, NULL, 0),
(10, '123344', 'fhtjjkkhuhdfyhh', '$2y$10$4G0e.IVURBaNjbIK3FSQEePjk7y3SYdHMmPR0tPz8FCO5TKH1M84S', '2025-02-25 09:14:56', 'staff', NULL, NULL, 0),
(11, 'Daniya', 'daniyafarha16@gmail.com', '$2y$10$99OrfcKQi9l29rEHG6MsOO2Vky9Xixhi9VhpbIOzN.U/IYOvoxRHS', '2025-02-27 07:31:58', 'student', NULL, NULL, 0),
(12, 'Kadeeja Mezwin', 'kmezwin@gmail.com', '$2y$10$MHYaG43gFRf6BmnnUuz9ze4hFLHcQYafzkL6G0RpoHkT5rS2bjone', '2025-02-27 07:44:00', 'student', NULL, NULL, 0),
(13, 'Reeha Sadique', 'reehasadique@gmail.com', '$2y$10$rn3ZxGR.yv5SB5YhL9fXcOjQfxv4EhVaKvw93xUQc3/KdO9hbxuyG', '2025-02-27 16:57:40', 'student', NULL, NULL, 0),
(14, 'Abdulla Sahl', 'minhasadique19@gmail.com', '$2y$10$FQuoj4rAzKE4K4MixrIa5uYnpKICq90rPyMGqrX1wEAa4A8AxpYuu', '2025-03-15 11:51:15', 'student', NULL, '930692', 1743010890),
(15, 'Pbr Campaign', 'pbrcampaignkkd@gmail.com', '$2y$10$fpIB0Xv425WyaBxqzFSqA.d543yNCuS2OzoD2EhWAg0yx85o8TKQ2', '2025-03-26 17:34:29', 'staff', NULL, '987115', 1743119026),
(16, 'Sadique A V', 'sadiq@gmail.com', '$2y$10$3Snc3k1aWSKLPNhrbEgo1.3xbHyBxuFT4Eo.CscV317TuCafwohgi', '2025-03-27 11:54:53', 'student', NULL, NULL, 0),
(17, 'Nidha Nishad', 'nidhanishad232@gmail.com', '$2y$10$4OKbAto/6KV1n421da8PUuzV/7DhshqZymUXRentMRJHuoZPswoQy', '2025-03-28 03:53:57', 'student', NULL, '265273', 1743134652),
(18, 'nashwa', 'nashwathoppil@gmail.com', '$2y$10$GZi8V3DcGykDSgDKyyHUW.tgScFXz7zz3Ep6mNitm4MExUcD25ZPe', '2025-03-28 03:59:46', 'staff', NULL, NULL, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `group_messages`
--
ALTER TABLE `group_messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `group_id` (`group_id`),
  ADD KEY `sender_id` (`sender_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `receiver_id` (`receiver_id`);

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `startups`
--
ALTER TABLE `startups`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `groups`
--
ALTER TABLE `groups`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `group_messages`
--
ALTER TABLE `group_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `startups`
--
ALTER TABLE `startups`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `group_messages`
--
ALTER TABLE `group_messages`
  ADD CONSTRAINT `group_messages_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  ADD CONSTRAINT `group_messages_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `posts`
--
ALTER TABLE `posts`
  ADD CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
