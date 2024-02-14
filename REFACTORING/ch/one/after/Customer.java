package ch.one.after;

import java.util.Enumeration;
import java.util.Vector;

public class Customer {
    private String name;
    private Vector rentals = new Vector();

    public void addRental(Rental rental){
        rentals.addElement(rental);
    }

    public String getName(){
        return name;
    }

    public String statement(){
        Enumeration rentalsEnumeration = rentals.elements();
        String result = getName() + " 고객님의 대여 기록\n";
        while(rentalsEnumeration.hasMoreElements()){
            Rental each = (Rental) rentalsEnumeration.nextElement();

            // 이번에 대여하는 비디오 정보와 대여료를 출력
            result += "\t" + each.getMovie().getTitle() + "\t" + each.getCharge() + "\n";
        }
        // 푸터 행 추가
        result += "누적 대여료: " + getTotalCharge() + "\n";
        result += "적립 포인트: " + getTotalFrequentRenterPoints();
        return result;
    }

    private double getTotalCharge(){
        double result = 0;
        Enumeration rentalsEnumeration = rentals.elements();

        while(rentalsEnumeration.hasMoreElements()){
            Rental each = (Rental) rentalsEnumeration.nextElement();
            result += each.getCharge();
        }
        return result;
    }

    private int getTotalFrequentRenterPoints(){
        int result = 0;
        Enumeration rentalsEnumeration = rentals.elements();

        while(rentalsEnumeration.hasMoreElements()){
            Rental each = (Rental) rentalsEnumeration.nextElement();
            result += each.getFrequentRenterPoints();
        }
        return result;
    }
}
