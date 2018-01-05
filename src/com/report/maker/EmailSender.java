package com.report.maker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class EmailSender {
	public static void main(String[] args) {
		// List<Student> students = readGradesXml();
		// EmailServerDetails emailServerDetails = getEmailServerDetails();
		//
		// // Prepare HTML Content
		// // for (int k = 0; k < students.size(); k++) {
		// Student student = students.get(0);
		// String htmlContent = getHtmlContent(student);
		// send_email(emailServerDetails, htmlContent, student);
		// }
	}

	public static EmailServerDetails getEmailServerDetails() {
		Properties props = new Properties();
		InputStream inputStream = null;
		EmailServerDetails emailServerDetails = new EmailServerDetails();
		try {
			inputStream = new FileInputStream("smtp.properties");

			// load a properties file
			props.load(inputStream);

			emailServerDetails.setHost(props.getProperty("smtp_host"));
			emailServerDetails.setSmtpPort(props.getProperty("smtp_port"));
			emailServerDetails.setTlsEnable(props.getProperty("tls_enable"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return emailServerDetails;
	}

	// Build HTML content here
	public static String getHtmlContent(Student student) {
		Properties props = new Properties();
		InputStream inputStream = null;
		String htmlContent = "";
		String targetColor = "yellow";
		String recent_sem_minus_two_name = "Fall 2016";
		String recent_sem_minus_one_nam = "Spring 2016";
		String recent_sem_name = "Fall 2017";
		
		try {
			inputStream = new FileInputStream("email.properties");

			// load a properties file
			props.load(inputStream);

			String htmlStyle = props.getProperty("html_style");
			htmlContent += htmlStyle;

			String baseConent = props.getProperty("base_content");
			htmlContent += baseConent;

			String criticalCoursesSection = props.getProperty("critical_courses_section_head");

			String criticalCourseText = "";
			int criticalCount = student.getCritical_course_count();
			if (criticalCount > 2) {
				targetColor = "red";
				criticalCourseText = props.getProperty("critical_course_3");
			} else if (criticalCount == 2) {
				targetColor = "orange";
				criticalCourseText = props.getProperty("critical_course_2");
			} else if (criticalCount == 1) {
				targetColor = "green";
				criticalCourseText = props.getProperty("critical_course_1");
			} else if (criticalCount == 0) {
				targetColor = "purple";
				criticalCourseText = props.getProperty("critical_course_0");
			}
			criticalCoursesSection = criticalCoursesSection.replaceFirst("TARGET", targetColor);
			htmlContent += criticalCoursesSection;

			String course_list = "";

			for (int i = 0; i < criticalCount; i++) {
				String course = "<li>" + student.getCritical_prog_courses().get(i) + "</li>";
				course_list += course;
			}
			htmlContent += course_list;

			String criticalCourseTail = props.getProperty("critical_courses_section_tail");
			htmlContent += criticalCourseTail;

			htmlContent += criticalCourseText;

			String repeatedCoursesSection = props.getProperty("repeated_courses_section_head");

			String repeatedCourseText = "";
			int repeatedCoursesCount = student.getRepeated_course_count();
			if (repeatedCoursesCount >= 1) {
				targetColor = "red";
				repeatedCourseText = props.getProperty("repeated_courses_section_list");
				String coursesList = "";
				for (int p = 0; p < repeatedCoursesCount; p++) {
					String course = "<li>" + student.getRepeated_courses().get(p) + "</li>";
					coursesList += course;
				}
				coursesList = "<ul>" + coursesList + "</ul>";
				repeatedCourseText = repeatedCourseText.replace("LIST", coursesList);
			} else {
				targetColor = "green";
				repeatedCourseText = props.getProperty("repeated_courses_section_none");
			}
			repeatedCoursesSection = repeatedCoursesSection.replaceFirst("TARGET", targetColor);

			htmlContent += repeatedCoursesSection;
			htmlContent += repeatedCourseText;
			
			String gpaTrendSection = props.getProperty("gpa_trend_head");

			double recent_sem_gpa = student.getRecent_sem().getSem_gpa();
			double recent_minus_one_sem_gpa = student.getRecent_minus_one_sem().getSem_gpa();
			double recent_minus_two_sem_gpa = student.getRecent_minus_two_sem().getSem_gpa();

			double recent_cum_gpa = student.getRecent_sem().getCum_gpa();
			double recent_minus_one_cum_gpa = student.getRecent_minus_one_sem().getCum_gpa();
			double recent_minus_two_cum_gpa = student.getRecent_minus_two_sem().getCum_gpa();

			if ((recent_sem_gpa - recent_minus_one_sem_gpa < 0
					&& recent_minus_one_sem_gpa - recent_minus_two_sem_gpa < 0)
					|| (recent_cum_gpa - recent_sem_gpa > 0.25))
				gpaTrendSection = gpaTrendSection.replaceFirst("TARGET", "red");
			else if ((recent_sem_gpa - recent_minus_one_sem_gpa < 0
					|| recent_minus_one_sem_gpa - recent_minus_two_sem_gpa < 0)
					|| (recent_cum_gpa - recent_sem_gpa > 0 && recent_cum_gpa - recent_sem_gpa < 0.25))
				gpaTrendSection = gpaTrendSection.replaceFirst("TARGET", "orange");
			else
				gpaTrendSection = gpaTrendSection.replaceFirst("TARGET", "green");

			gpaTrendSection = gpaTrendSection.replaceFirst("Semester_Minus_Two", recent_sem_minus_two_name);
			gpaTrendSection = gpaTrendSection.replaceFirst("Semester_Minus_One", recent_sem_minus_one_nam);
			gpaTrendSection = gpaTrendSection.replaceFirst("Semester_Current", recent_sem_name);

			gpaTrendSection = gpaTrendSection.replaceFirst("GPA_1",
					String.valueOf(student.getRecent_minus_two_sem().getSem_gpa()));
			gpaTrendSection = gpaTrendSection.replaceFirst("GPA_2",
					String.valueOf(student.getRecent_minus_one_sem().getSem_gpa()));
			gpaTrendSection = gpaTrendSection.replaceFirst("GPA_3",
					String.valueOf(student.getRecent_sem().getSem_gpa()));

			gpaTrendSection = gpaTrendSection.replaceFirst("CUM_1",
					String.valueOf(student.getRecent_minus_two_sem().getCum_gpa()));
			gpaTrendSection = gpaTrendSection.replaceFirst("CUM_2",
					String.valueOf(student.getRecent_minus_one_sem().getCum_gpa()));
			gpaTrendSection = gpaTrendSection.replaceFirst("CUM_3",
					String.valueOf(student.getRecent_sem().getCum_gpa()));

			if (recent_sem_gpa - recent_minus_one_sem_gpa > 0)
				gpaTrendSection = gpaTrendSection.replaceFirst("GPA_Symbol", "-->");
			else if (recent_sem_gpa - recent_minus_one_sem_gpa < 0)
				gpaTrendSection = gpaTrendSection.replaceFirst("GPA_Symbol", "<--");
			else if (recent_sem_gpa - recent_minus_one_sem_gpa < 0)
				gpaTrendSection = gpaTrendSection.replaceFirst("GPA_Symbol", "^");

			if (recent_cum_gpa - recent_minus_one_cum_gpa > 0)
				gpaTrendSection = gpaTrendSection.replaceFirst("CUM_Symbol", "-->");
			else if (recent_cum_gpa - recent_minus_one_cum_gpa < 0)
				gpaTrendSection = gpaTrendSection.replaceFirst("CUM_Symbol", "<--");
			else if (recent_cum_gpa - recent_minus_one_cum_gpa < 0)
				gpaTrendSection = gpaTrendSection.replaceFirst("CUM_Symbol", "^");

			htmlContent += gpaTrendSection;

			String gpaTrendTailSection = "";
			double gpa_diff = recent_sem_gpa - recent_minus_one_sem_gpa;
			if (recent_cum_gpa > 2.99 && (gpa_diff) >= 0)
				gpaTrendTailSection = props.getProperty("gpa_trend_good");
			else if (recent_cum_gpa < 3 && (gpa_diff) > 0.25)
				gpaTrendTailSection = props.getProperty("gpa_trend_moving_up");
			else if (recent_cum_gpa < 3 && (gpa_diff > 0 && gpa_diff < 0.25))
				gpaTrendTailSection = props.getProperty("gpa_trend_flat");
			else
				gpaTrendTailSection = props.getProperty("gpa_trend_moving_down");
			htmlContent += gpaTrendTailSection;
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return htmlContent;
	}

	public static void send_email(EmailServerDetails serverDetails, String htmlContent, Student student) {

		// Recipient's email ID
		String to = student.getEmail();

		// Sender's email ID
		String from = serverDetails.getUsername();
		final String username = serverDetails.getUsername();
		final String password = serverDetails.getPassword();

		String host = serverDetails.getHost();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", serverDetails.getTlsEnable());
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", serverDetails.getSmtpPort());

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Semester Report");

			// Send the actual HTML message, as big as you like
			message.setContent(htmlContent, "text/html");

			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static List<Student> readGradesXml(String path) {
		List<Student> students = new ArrayList<Student>();
		try {
			FileInputStream excelFile = new FileInputStream(new File(path));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet gradesSheet = workbook.getSheetAt(0);
			Sheet repeatedCoursesSheet = workbook.getSheetAt(1);
			Sheet gpaScoreSheet = workbook.getSheetAt(2);

			Iterator<Row> iterator = gradesSheet.iterator();

			int rowCount = 1;
			Row currentRow = iterator.next();
			int[] courses_list = new int[] { 12, 14, 16, 18 };
			int[] repeated_list = new int[] { 1, 2, 3, 4 };
			currentRow = iterator.next();
			while (iterator.hasNext()) {
				Student currentStudent = new Student();
				currentRow = iterator.next();
				if (currentRow.getCell(0) != null) {

					currentStudent.setName(getStringCellValue(currentRow, 2));
					currentStudent.setEmail(getStringCellValue(currentRow, 3));

					int critical_course_count = getNumericCellValue(currentRow, 9);
					currentStudent.setCritical_course_count(critical_course_count);
					List<String> critical_courses = new ArrayList<String>();
					for (int k = 0; k < critical_course_count; k++) {
						int course_no = courses_list[k];
						critical_courses.add(getStringCellValue(currentRow, course_no));
					}
					currentStudent.setCritical_prog_courses(critical_courses);
					currentRow = repeatedCoursesSheet.getRow(rowCount);
					int repeated_course_count = getNumericCellValue(currentRow, 0);
					rowCount++;
					currentStudent.setRepeated_course_count(repeated_course_count);

					List<String> repeated_courses = new ArrayList<String>();
					for (int k = 0; k < repeated_course_count; k++) {
						int course_no = repeated_list[k];
						repeated_courses.add(getStringCellValue(currentRow, course_no));
					}

					currentStudent.setRepeated_courses(repeated_courses);
					
					currentRow = gpaScoreSheet.getRow(rowCount);
					Semester recent_minus_two = new Semester();
					recent_minus_two.setSem_gpa(getDoubleCellValue(currentRow, 0));
					recent_minus_two.setCum_gpa(getDoubleCellValue(currentRow, 1));
					currentStudent.setRecent_minus_two_sem(recent_minus_two);

					Semester recent_minus_one = new Semester();
					recent_minus_one.setSem_gpa(getDoubleCellValue(currentRow, 2));
					recent_minus_one.setCum_gpa(getDoubleCellValue(currentRow, 3));
					currentStudent.setRecent_minus_one_sem(recent_minus_one);

					Semester current_sem = new Semester();
					current_sem.setSem_gpa(getDoubleCellValue(currentRow, 4));
					current_sem.setCum_gpa(getDoubleCellValue(currentRow, 5));
					currentStudent.setRecent_sem(current_sem);
				} else {
					break;
				}
				students.add(currentStudent);
			}
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return students;
	}

	public static int getNumericCellValue(Row currentRow, int index) {
		int value = (int) currentRow.getCell(index).getNumericCellValue();
		return value;
	}

	public static String getStringCellValue(Row currentRow, int index) {
		String value = currentRow.getCell(index).getStringCellValue();
		return value;

	}
	
	public static double getDoubleCellValue(Row currentRow, int index) {
		if (currentRow.getCell(index) != null) {
			double value = currentRow.getCell(index).getNumericCellValue();
			System.out.print("\n " + value);
			return value;
		} else
			return 0;
	}
}
