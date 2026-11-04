package com.flatbook.config;

import com.flatbook.entity.Flat;
import com.flatbook.entity.User;
import com.flatbook.repository.FlatRepository;
import com.flatbook.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FlatRepository flatRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           FlatRepository flatRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.flatRepository = flatRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Create Admin if not exists
        if (userRepository.findByEmail("admin@flatbook.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@flatbook.com");
            admin.setMobile("9999999999");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println(">>> Admin created: admin@flatbook.com / admin123");
        }

        // Create sample customer
        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User customer = new User();
            customer.setFullName("Rahul Sharma");
            customer.setEmail("user@example.com");
            customer.setMobile("9876543210");
            customer.setPassword(passwordEncoder.encode("user123"));
            customer.setRole(User.Role.CUSTOMER);
            customer.setActive(true);
            userRepository.save(customer);
            System.out.println(">>> Customer created: user@example.com / user123");
        }

        // Sample flats
        if (flatRepository.count() == 0) {
            createFlat("Spacious 2BHK in Koramangala", "Beautiful fully furnished 2BHK apartment with modern amenities near IT parks.",
                    "Bangalore", "Koramangala", "5th Block, Koramangala", "2BHK", 1200,
                    new BigDecimal("35000"), "MONTHLY", "Furnished",
                    "Parking,Lift,Gym,Security,Power Backup", "/images/flat1.jpg");

            createFlat("Luxury 3BHK Sea View", "Premium 3BHK apartment with sea view and club house access.",
                    "Mumbai", "Bandra West", "Carter Road, Bandra West", "3BHK", 1850,
                    new BigDecimal("95000"), "MONTHLY", "Furnished",
                    "Parking,Lift,Gym,Pool,Security,Club House", "/images/flat2.jpg");

            createFlat("Affordable 1BHK Near Metro", "Well maintained 1BHK ideal for working professionals.",
                    "Delhi", "Dwarka", "Sector 12, Dwarka", "1BHK", 650,
                    new BigDecimal("18000"), "MONTHLY", "Semi-Furnished",
                    "Parking,Lift,Security", "/images/flat3.jpg");

            createFlat("Modern Studio Apartment", "Compact studio perfect for singles, fully equipped kitchen.",
                    "Pune", "Hinjewadi", "Phase 1, Hinjewadi", "Studio", 450,
                    new BigDecimal("15000"), "MONTHLY", "Furnished",
                    "Parking,Lift,Security,WiFi", "/images/flat4.jpg");

            createFlat("Family 3BHK with Garden", "Spacious 3BHK independent floor with private garden.",
                    "Hyderabad", "Gachibowli", "Near DLF Cyber City", "3BHK", 2100,
                    new BigDecimal("42000"), "MONTHLY", "Semi-Furnished",
                    "Parking,Garden,Security,Power Backup", "/images/flat5.jpg");

            createFlat("2BHK Ready to Move", "Newly constructed 2BHK with modular kitchen.",
                    "Chennai", "OMR", "Sholinganallur, OMR", "2BHK", 1100,
                    new BigDecimal("28000"), "MONTHLY", "Unfurnished",
                    "Parking,Lift,Security", "/images/flat6.jpg");

            System.out.println(">>> 6 sample flats created successfully");
        }
    }

    private void createFlat(String title, String desc, String city, String locality, String address,
                            String bhk, int area, BigDecimal price, String priceType,
                            String furnished, String amenities, String image) {
        Flat f = new Flat();
        f.setTitle(title);
        f.setDescription(desc);
        f.setCity(city);
        f.setLocality(locality);
        f.setAddress(address);
        f.setBhkType(bhk);
        f.setAreaSqft(area);
        f.setPrice(price);
        f.setPriceType(priceType);
        f.setFurnishedStatus(furnished);
        f.setAmenities(amenities);
        f.setImageUrl(image);
        f.setStatus(Flat.Status.AVAILABLE);
        flatRepository.save(f);
    }
}
