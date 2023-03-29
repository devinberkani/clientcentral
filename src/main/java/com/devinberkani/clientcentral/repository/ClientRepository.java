package com.devinberkani.clientcentral.repository;

import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // retrieve page of client objects that matches a search query (could be empty) and belongs to the logged-in user
    @Query("SELECT c FROM Client c WHERE c.user.id = :userId AND (CONCAT(LOWER(c.firstName),' ',LOWER(c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Client> findMatchingClients(@Param("userId") Long userId, @Param("query") String query, Pageable pageable);
    Client findClientById(Long id);
    @Query("SELECT c FROM Client c WHERE (MONTH(c.birthday) = MONTH(CURRENT_DATE()) AND (DAY(c.birthday) = DAY(CURRENT_DATE()))) AND c.user = :user")
    Page<Client> getTodayBirthdays(@Param("user") User user, Pageable pageable);

    /*

    EXPLANATION OF BELOW (ASC) METHOD FOR GETTING UPCOMING BIRTHDAYS

    CASE expression checks if the client's birthday is after today's date based on the month and day. If the month of the client's birthday is greater than the current month or if the month is the same and the day is greater, the expression returns 0. Otherwise, it returns 1. Sorting by this expression ensures that clients with upcoming birthdays are listed before clients with birthdays that have already passed this year.

    After the CASE expression, we sort the results based on the month and day of the client's birthday. This ensures that clients with the same "upcoming" or "past" status are sorted by their birthdays.

    The first method sorts the results in the following order:

    Upcoming birthdays (where the CASE expression evaluates to 0) appear first.
    Within the upcoming birthdays, clients are sorted by the month and day of their birthday in ascending order (from the earliest to the latest).
    Past birthdays (where the CASE expression evaluates to 1) appear after upcoming birthdays.
    Within the past birthdays, clients are also sorted by the month and day of their birthday in ascending order.

    */

    @Query("SELECT c FROM Client c WHERE c.user = :user AND (MONTH(c.birthday) != MONTH(CURRENT_DATE()) OR DAY(c.birthday) != DAY(CURRENT_DATE())) ORDER BY (CASE WHEN MONTH(c.birthday) > MONTH(CURRENT_DATE()) OR (MONTH(c.birthday) = MONTH(CURRENT_DATE()) AND DAY(c.birthday) > DAY(CURRENT_DATE())) THEN 0 ELSE 1 END), MONTH(c.birthday), DAY(c.birthday)")
    Page<Client> getUpcomingBirthdaysAsc(@Param("user") User user, Pageable pageable);


    /*

    EXPLANATION OF BELOW (DESC) METHOD FOR GETTING UPCOMING BIRTHDAYS

    CASE expression checks if the client's birthday is after today's date based on the month and day. If the month of the client's birthday is greater than the current month or if the month is the same and the day is greater, the expression returns 1. Otherwise, it returns 0. Sorting by this expression ensures that clients with upcoming birthdays are listed after clients with birthdays that have already passed this year.

    After the CASE expression, we sort the results based on the month and day of the client's birthday in descending order. This ensures that clients with the same "upcoming" or "past" status are sorted by their birthdays in reverse order.

    This method sorts the results in the reverse order compared to the first method:

    Past birthdays (where the CASE expression evaluates to 0) appear first.
    Within the past birthdays, clients are sorted by the month and day of their birthday in descending order (from the latest to the earliest).
    Upcoming birthdays (where the CASE expression evaluates to 1) appear after past birthdays.
    Within the upcoming birthdays, clients are sorted by the month and day of their birthday in descending order.

    */

    @Query("SELECT c FROM Client c WHERE c.user = :user AND (MONTH(c.birthday) != MONTH(CURRENT_DATE()) OR DAY(c.birthday) != DAY(CURRENT_DATE())) ORDER BY (CASE WHEN MONTH(c.birthday) > MONTH(CURRENT_DATE()) OR (MONTH(c.birthday) = MONTH(CURRENT_DATE()) AND DAY(c.birthday) > DAY(CURRENT_DATE())) THEN 1 ELSE 0 END), MONTH(c.birthday) DESC, DAY(c.birthday) DESC")
    Page<Client> getUpcomingBirthdaysDesc(@Param("user") User user, Pageable pageable);


}
