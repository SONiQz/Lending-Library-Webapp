package org.example.library.controller;

import org.example.library.dto.userLoanFrequencyDTO;
import org.example.library.model.warehouse.warehouseBookModel;
import org.example.library.repository.warehouseDailyStatsRepository;
import org.example.library.repository.OperationalUserRepository;
import org.example.library.model.operational.User;
import org.example.library.service.DailyStatsService;
import org.example.library.service.DataCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

// Is the method for calling functions to generate figures from different repositories to generate stats/values or
// other events (i.e. user registration)

@Controller
public class AppController {

    // Call external libraries that are required
    private final DataCopyService dataCopyService;
    private final DailyStatsService dailyStatsService;
    private final OperationalUserRepository userRepo;

    // Call repositories required for the libraries
    @Autowired
    private org.example.library.repository.warehouseLoanRepository warehouseLoanRepository;
    @Autowired
    private warehouseDailyStatsRepository warehouseDailyStatsRepository;
    @Qualifier("warehouseEntityManagerFactory")
    @Autowired
    private LocalContainerEntityManagerFactoryBean warehouseEntityManagerFactory;
    @Qualifier("warehouseJdbcTemplate")
    @Autowired
    private JdbcTemplate warehouseJdbcTemplate;
    @Autowired
    private org.example.library.repository.warehouseBookRepositoryImpl warehouseBookRepositoryImpl;
    @Autowired
    private org.example.library.repository.warehouseDailyStatsRepositoryImpl warehouseDailyStatsRepositoryImpl;
    @Autowired
    private org.example.library.repository.warehouseBookRepository warehouseBookRepository;
    @Autowired
    private org.example.library.repository.warehouseLoanRepositoryImpl warehouseLoanRepositoryImpl;

    @Autowired
    public AppController(DataCopyService dataCopyService, DailyStatsService dailyStatsService, OperationalUserRepository userRepo) {
        this.dataCopyService = dataCopyService;
        this.dailyStatsService = dailyStatsService;
        this.userRepo = userRepo;
    }

    // Define homepage
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    // Define User Registration page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    //Define Data Operations Page
    @GetMapping("/data")
    public String viewDataPage() {
        return "data";
    }

    //Manual Method for Copying data to staging, then to warehouse
    @PostMapping("/data/copy-to-staging")
    public String copyData() {
        dataCopyService.copyDataFromStagingToWarehouse();
        return "redirect:/data";
    }

    // Generate Daily Stats Records
    @PostMapping("/data/daily-stats")
    public String dailyStats() {
        dailyStatsService.saveDailyStats();
        return "redirect:/data";
    }

    // Generate User Accounts
    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "register_success";
    }

    // Provide logged in users with a page (Showing list of users)
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    // Selection of Reports
    @GetMapping("/reports")
    public String showTopBooks(Model model) {


        return "topBooks";
    }

    // Date selector for Additional Reports
    @GetMapping("/stats")
    public String showForm() {
        return "statsForm";
    }

    // Function to generate stats from the warehouse data
    @PostMapping("/stats")
    public String showStats(
            // Set the date format for OracleSQL
            @RequestParam("startDate") @DateTimeFormat(pattern = "dd-MMM-yyyy") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MMM-yyyy") LocalDate endDate,
            Model model) {

        // Get sum of users by date
        long userSumDate = warehouseDailyStatsRepository.sumUsersByDate(startDate, endDate);
        model.addAttribute("userSumDate", userSumDate);

        // Get total users
        long sumUsers = warehouseDailyStatsRepository.sumUsers();
        model.addAttribute("sumUsers", sumUsers);

        // Get Average Loan frequency per loaned book
        float avgLoanFrequency = warehouseLoanRepositoryImpl.avgLoanFrequency();
        model.addAttribute("avgLoanFrequency", avgLoanFrequency);

        // Get Average Loan Duration
        double avgLoanDuration = warehouseLoanRepositoryImpl.avgLoanDuration(startDate, endDate);
        model.addAttribute("avgLoanDuration", avgLoanDuration);

        //Get books that have been edited by range
        List<warehouseBookModel> booksEditedByRange = warehouseBookRepositoryImpl.booksEditedByRange(startDate, endDate);
        model.addAttribute("booksEditedByRange", booksEditedByRange);

        // Count of books from dailystats
        long countBooksByRange = warehouseBookRepository.countBooksByRange(startDate, endDate);
        model.addAttribute("countBooksByRange", countBooksByRange);

        // Total fees generated between dates
        float feesForDateRange = warehouseLoanRepositoryImpl.feesForDateRange(startDate, endDate);
        model.addAttribute("feesForDateRange", feesForDateRange);

        // Number of books added between dates from Daily stats
        int addedBooks = warehouseDailyStatsRepository.addedBooks(startDate, endDate);
        model.addAttribute("addedBooks", addedBooks);

        // Get the most frequent loaners
        List<userLoanFrequencyDTO> frequentUsers = warehouseLoanRepositoryImpl.mostFrequentUsers(startDate, endDate);
        model.addAttribute("frequentUsers", frequentUsers);

        model.addAttribute("books", warehouseBookRepositoryImpl.findTopBooks());

        long bookCount = warehouseBookRepositoryImpl.countBooks();
        model.addAttribute("bookCount", bookCount);

        return "statsResults";
    }
}
