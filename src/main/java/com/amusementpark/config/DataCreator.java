package com.amusementpark.config;

import com.amusementpark.model.*;
import com.amusementpark.model.attraction.*;
import com.amusementpark.model.embeddable.ContactInfo;
import com.amusementpark.model.employee.*;
import com.amusementpark.model.enums.*;
import com.amusementpark.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataCreator implements CommandLineRunner {

    private final ZoneRepository zoneRepository;
    private final AttractionRepository attractionRepository;
    private final ShopRepository shopRepository;
    private final MaintenanceStaffRepository maintenanceStaffRepository;
    private final RideOperatorRepository rideOperatorRepository;
    private final ManagerRepository managerRepository;
    private final ShiftRepository shiftRepository;
    private final MalfunctionReportRepository malfunctionReportRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final PromotionRepository promotionRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (zoneRepository.count() > 0) {
            System.out.println("Database already created");
            return;
        }

        System.out.println("Creating database");

        Zone fantasyLand = Zone.builder()
                .name("Fantasy Land")
                .theme("Fairy tales and magic")
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(21, 0))
                .build();

        Zone adventureBay = Zone.builder()
                .name("Adventure Bay")
                .theme("Pirates and water")
                .openingTime(LocalTime.of(10, 0))
                .closingTime(LocalTime.of(20, 0))
                .build();

        Zone thrillMountain = Zone.builder()
                .name("Thrill Mountain")
                .theme("Extreme rides")
                .openingTime(LocalTime.of(10, 0))
                .closingTime(LocalTime.of(22, 0))
                .build();

        Zone vipLounge = Zone.builder()
                .name("VIP Lounge")
                .theme("Exclusive experience")
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(23, 0))
                .build();

        zoneRepository.saveAll(List.of(fantasyLand, adventureBay, thrillMountain, vipLounge));


        RollerCoaster dragonFlight = RollerCoaster.builder()
                .name("Dragon Flight")
                .doesVipPassWork(false)
                .attractionHeight(25)
                .maxSeats(24)
                .status(AttractionStatus.ACTIVE)
                .intensity(Intensity.EXTREME)
                .minHeight(140)
                .safetyRestrictions("Not suitable for people with back problems")
                .zone(fantasyLand)
                .trackLength(850.0)
                .maxSpeed(95.0)
                .numberOfLoops(3)
                .build();

        WaterRide enchantedRiver = WaterRide.builder()
                .name("Enchanted River")
                .doesVipPassWork(false)
                .attractionHeight(5)
                .maxSeats(20)
                .status(AttractionStatus.ACTIVE)
                .intensity(Intensity.LIGHT)
                .hasAnimators(true)
                .isParentSupervisionRequired(false)
                .zone(fantasyLand)
                .waterDepth(1.2)
                .waterTemperature(26.0)
                .build();

        WaterRide pirateSplash = WaterRide.builder()
                .name("Pirate Splash")
                .doesVipPassWork(false)
                .attractionHeight(15)
                .maxSeats(30)
                .status(AttractionStatus.ACTIVE)
                .intensity(Intensity.EXTREME)
                .minHeight(120)
                .zone(adventureBay)
                .waterDepth(3.5)
                .waterTemperature(24.0)
                .build();

        RollerCoaster jungleRush = RollerCoaster.builder()
                .name("Jungle Rush")
                .doesVipPassWork(false)
                .attractionHeight(30)
                .maxSeats(20)
                .status(AttractionStatus.ACTIVE)
                .intensity(Intensity.EXTREME)
                .minHeight(130)
                .safetyRestrictions("Hold on tight")
                .zone(adventureBay)
                .trackLength(620.0)
                .maxSpeed(80.0)
                .numberOfLoops(2)
                .build();

        RollerCoaster thunderCoaster = RollerCoaster.builder()
                .name("Thunder Coaster")
                .doesVipPassWork(true)
                .attractionHeight(45)
                .maxSeats(28)
                .status(AttractionStatus.ACTIVE)
                .intensity(Intensity.EXTREME)
                .minHeight(150)
                .safetyRestrictions("Not for pregnant women or heart conditions")
                .zone(thrillMountain)
                .trackLength(1200.0)
                .maxSpeed(130.0)
                .numberOfLoops(5)
                .build();

        RollerCoaster stormChaser = RollerCoaster.builder()
                .name("Storm Chaser")
                .doesVipPassWork(true)
                .attractionHeight(40)
                .maxSeats(24)
                .status(AttractionStatus.ACTIVE)
                .intensity(Intensity.EXTREME)
                .minHeight(145)
                .zone(thrillMountain)
                .trackLength(980.0)
                .maxSpeed(115.0)
                .numberOfLoops(4)
                .build();

        FourDRide timeWarp = FourDRide.builder()
                .name("Time Warp 4D")
                .doesVipPassWork(true)
                .attractionHeight(10)
                .maxSeats(40)
                .status(AttractionStatus.ACTIVE)
                .intensity(Intensity.LIGHT)
                .hasAnimators(false)
                .isParentSupervisionRequired(false)
                .zone(vipLounge)
                .showDuration(15.0)
                .effectTypes(new HashSet<>(Set.of("Wind", "Water Spray", "Motion", "Scent")))
                .build();

        FourDRide galaxyExplorer = FourDRide.builder()
                .name("Galaxy Explorer 4D")
                .doesVipPassWork(true)
                .attractionHeight(10)
                .maxSeats(50)
                .status(AttractionStatus.ACTIVE)
                .intensity(Intensity.EXTREME)
                .minHeight(110)
                .zone(vipLounge)
                .showDuration(20.0)
                .effectTypes(new HashSet<>(Set.of("Lightning", "Vibration", "Smoke")))
                .build();

        attractionRepository.saveAll(List.of(
                dragonFlight, enchantedRiver, pirateSplash, jungleRush,
                thunderCoaster, stormChaser, timeWarp, galaxyExplorer
        ));

        Shop magicShop = Shop.builder()
                .name("Magic Emporium")
                .type(ShopType.MERCHANDISE)
                .location("Fantasy Land entrance")
                .zone(fantasyLand)
                .build();

        Shop fairyTreats = Shop.builder()
                .name("Fairy Treats")
                .type(ShopType.FOOD)
                .location("Fantasy Land central square")
                .zone(fantasyLand)
                .build();

        Shop pirateGrog = Shop.builder()
                .name("Pirate Grog")
                .type(ShopType.BEVERAGES)
                .location("Adventure Bay dock")
                .zone(adventureBay)
                .build();

        Shop quickBites = Shop.builder()
                .name("Quick Bites")
                .type(ShopType.VENDING_MACHINE)
                .location("Thrill Mountain base")
                .zone(thrillMountain)
                .build();

        Shop vipBar = Shop.builder()
                .name("VIP Sky Bar")
                .type(ShopType.BEVERAGES)
                .location("VIP Lounge rooftop")
                .zone(vipLounge)
                .build();

        shopRepository.saveAll(List.of(magicShop, fairyTreats, pirateGrog, quickBites, vipBar));


        MaintenanceStaff johnSmith = MaintenanceStaff.builder()
                .name("John")
                .surname("Smith")
                .yearsOfExperience(8)
                .birthDate(LocalDate.of(1985, 3, 15))
                .specialization("Electrical Systems")
                .certifications(new HashSet<>(Set.of("Electrical Safety Level 3", "First Aid", "Fire Safety")))
                .build();

        MaintenanceStaff annaBrown = MaintenanceStaff.builder()
                .name("Anna")
                .surname("Brown")
                .yearsOfExperience(5)
                .birthDate(LocalDate.of(1990, 7, 22))
                .specialization("Mechanical Engineering")
                .certifications(new HashSet<>(Set.of("Hydraulic Systems", "Welding Certificate")))
                .build();

        maintenanceStaffRepository.saveAll(List.of(johnSmith, annaBrown));

        RideOperator mikeJohnson = RideOperator.builder()
                .name("Mike")
                .surname("Johnson")
                .yearsOfExperience(3)
                .birthDate(LocalDate.of(1995, 1, 10))
                .operatorLicenceId("OP-001")
                .firstAidCertified(true)
                .build();

        RideOperator sarahDavis = RideOperator.builder()
                .name("Sarah")
                .surname("Davis")
                .yearsOfExperience(2)
                .birthDate(LocalDate.of(1998, 11, 5))
                .operatorLicenceId("OP-002")
                .firstAidCertified(false)
                .build();

        rideOperatorRepository.saveAll(List.of(mikeJohnson, sarahDavis));

        Manager robertWilson = Manager.builder()
                .name("Robert")
                .surname("Wilson")
                .yearsOfExperience(10)
                .birthDate(LocalDate.of(1980, 6, 20))
                .build();

        managerRepository.save(robertWilson);

        Shift shift1 = Shift.builder()
                .date(LocalDate.of(2026, 7, 15))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(16, 0))
                .employee(mikeJohnson)
                .attraction(thunderCoaster)
                .assignedByManager(robertWilson)
                .build();

        Shift shift2 = Shift.builder()
                .date(LocalDate.of(2026, 7, 15))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(18, 0))
                .employee(sarahDavis)
                .attraction(pirateSplash)
                .assignedByManager(robertWilson)
                .build();

        Shift shift3 = Shift.builder()
                .date(LocalDate.of(2026, 7, 15))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .employee(johnSmith)
                .attraction(dragonFlight)
                .assignedByManager(robertWilson)
                .build();

        Shift shift4 = Shift.builder()
                .date(LocalDate.of(2026, 7, 16))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(16, 0))
                .employee(mikeJohnson)
                .attraction(stormChaser)
                .assignedByManager(robertWilson)
                .build();

        shiftRepository.saveAll(List.of(shift1, shift2, shift3, shift4));

        MalfunctionReport report1 = MalfunctionReport.builder()
                .datePerformed(LocalDate.of(2026, 7, 10))
                .description("Hydraulic brake system showed irregular pressure readings. Replaced brake pads and recalibrated sensors")
                .attraction(thunderCoaster)
                .maintenanceStaff(johnSmith)
                .build();

        MalfunctionReport report2 = MalfunctionReport.builder()
                .datePerformed(LocalDate.of(2026, 7, 12))
                .description("Water pump malfunction causing low flow rate. Replaced pump motor and tested water circulation")
                .attraction(pirateSplash)
                .maintenanceStaff(annaBrown)
                .build();

        malfunctionReportRepository.saveAll(List.of(report1, report2));

        TicketType dayPass = TicketType.builder()
                .typeName("Day Pass")
                .isVip(false)
                .initialPrice(49.99)
                .zonesByName(new HashMap<>(Map.of(
                        fantasyLand.getName(), fantasyLand,
                        adventureBay.getName(), adventureBay
                )))
                .build();

        TicketType weekendPass = TicketType.builder()
                .typeName("Weekend Pass")
                .isVip(false)
                .initialPrice(89.99)
                .zonesByName(new HashMap<>(Map.of(
                        fantasyLand.getName(), fantasyLand,
                        adventureBay.getName(), adventureBay,
                        thrillMountain.getName(), thrillMountain
                )))
                .build();

        TicketType vipAllAccess = TicketType.builder()
                .typeName("VIP All Access")
                .isVip(true)
                .initialPrice(149.99)
                .zonesByName(new HashMap<>(Map.of(
                        fantasyLand.getName(), fantasyLand,
                        adventureBay.getName(), adventureBay,
                        thrillMountain.getName(), thrillMountain,
                        vipLounge.getName(), vipLounge
                )))
                .build();

        TicketType familyPack = TicketType.builder()
                .typeName("Family Pack")
                .isVip(false)
                .initialPrice(129.99)
                .zonesByName(new HashMap<>(Map.of(
                        fantasyLand.getName(), fantasyLand,
                        adventureBay.getName(), adventureBay
                )))
                .build();

        TicketType seasonPass = TicketType.builder()
                .typeName("Season Pass")
                .isVip(false)
                .initialPrice(299.99)
                .zonesByName(new HashMap<>(Map.of(
                        fantasyLand.getName(), fantasyLand,
                        adventureBay.getName(), adventureBay,
                        thrillMountain.getName(), thrillMountain
                )))
                .build();

        ticketTypeRepository.saveAll(List.of(dayPass, weekendPass, vipAllAccess, familyPack, seasonPass));

        Promotion summerSale = Promotion.builder()
                .name("Summer Sale")
                .startDate(LocalDate.of(2026, 6, 1))
                .endDate(LocalDate.of(2026, 8, 31))
                .promotionPercentage(15)
                .build();

        Promotion earlyBird = Promotion.builder()
                .name("Early Bird")
                .startDate(LocalDate.of(2026, 7, 1))
                .endDate(LocalDate.of(2026, 7, 31))
                .promotionPercentage(10)
                .build();

        Promotion familyDeal = Promotion.builder()
                .name("Family Deal")
                .startDate(LocalDate.of(2026, 6, 15))
                .endDate(LocalDate.of(2026, 9, 15))
                .promotionPercentage(20)
                .build();

        Promotion winterSpecial = Promotion.builder()
                .name("Winter Special")
                .startDate(LocalDate.of(2026, 12, 1))
                .endDate(LocalDate.of(2026, 12, 31))
                .promotionPercentage(25)
                .build();

        promotionRepository.saveAll(List.of(summerSale, earlyBird, familyDeal, winterSpecial));

        Customer alice = Customer.builder()
                .name("Alice")
                .surname("Cooper")
                .contactInfo(new ContactInfo("+48 396 578 127", "alice.cooper@gmail.com"))
                .build();

        Customer bob = Customer.builder()
                .name("Bob")
                .surname("Miller")
                .contactInfo(new ContactInfo("+48 686 777 900", "bob.miller@gmail.com"))
                .build();

        Customer carol = Customer.builder()
                .name("Carol")
                .surname("White")
                .contactInfo(new ContactInfo("+48  548 690 543", "carol.white@gmail.com"))
                .build();

        customerRepository.saveAll(List.of(alice, bob, carol));


        Order aliceOrder = Order.builder()
                .orderNumber("ORD-711868")
                .status(OrderStatus.COMPLETED)
                .paymentMethod("Credit Card")
                .personalDiscount(20)
                .customer(alice)
                .promotion(summerSale)
                .build();

        orderRepository.save(aliceOrder);

        Ticket aliceTicket1 = Ticket.builder()
                .startDate(LocalDate.of(2026, 7, 15))
                .endDate(LocalDate.of(2026, 7, 15))
                .quantity(2)
                .ticketType(dayPass)
                .order(aliceOrder)
                .build();

        Ticket aliceTicket2 = Ticket.builder()
                .startDate(LocalDate.of(2026, 7, 15))
                .endDate(LocalDate.of(2026, 7, 17))
                .quantity(1)
                .ticketType(vipAllAccess)
                .order(aliceOrder)
                .build();

        ticketRepository.saveAll(List.of(aliceTicket1, aliceTicket2));

        Order bobOrder = Order.builder()
                .orderNumber("ORD-892145")
                .status(OrderStatus.IN_PROGRESS)
                .customer(bob)
                .build();

        orderRepository.save(bobOrder);

        Ticket bobTicket1 = Ticket.builder()
                .startDate(LocalDate.of(2026, 7, 18))
                .endDate(LocalDate.of(2026, 7, 19))
                .quantity(1)
                .ticketType(weekendPass)
                .order(bobOrder)
                .build();

        Ticket bobTicket2 = Ticket.builder()
                .startDate(LocalDate.of(2026, 7, 18))
                .endDate(LocalDate.of(2026, 7, 18))
                .quantity(3)
                .ticketType(familyPack)
                .order(bobOrder)
                .build();

        ticketRepository.saveAll(List.of(bobTicket1, bobTicket2));

        System.out.println(" ");
        System.out.println("  Zones: " + zoneRepository.count());
        System.out.println("  Attractions: " + attractionRepository.count());
        System.out.println("  Shops: " + shopRepository.count());
        System.out.println("  Employees: " + (maintenanceStaffRepository.count() + rideOperatorRepository.count() + managerRepository.count()));
        System.out.println("  Shifts: " + shiftRepository.count());
        System.out.println("  Malfunction Reports: " + malfunctionReportRepository.count());
        System.out.println("  Ticket Types: " + ticketTypeRepository.count());
        System.out.println("  Promotions: " + promotionRepository.count());
        System.out.println("  Customers: " + customerRepository.count());
        System.out.println("  Orders: " + orderRepository.count());
        System.out.println("  Tickets: " + ticketRepository.count());
    }
}
