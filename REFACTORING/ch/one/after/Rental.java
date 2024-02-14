package ch.one.after;

public class Rental {
    private Movie movie;
    private int daysRented;

    public Rental(Movie movie, int daysRented){
        this.movie = movie;
        this.daysRented = daysRented;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getDaysRented() {
        return daysRented;
    }

    // 최신물을 이틀 이상 대여하면 2포인트를 지급하고 그 외엔 1포인트 지급하는 코드를
    // 빼내 getFrequentRenterPoints 메서드로 만들고 이 Rental 클래스로 옮김
    public int getFrequentRenterPoints(){
        return movie.getFrequentRenterPoints(daysRented);
    }
    public double getCharge(){
        return movie.getCharge(daysRented);
    }
}
