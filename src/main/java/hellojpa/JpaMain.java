package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Movie movie = new Movie();
            movie.setDirector("aaaa");
            movie.setActor("bbbb");
            movie.setName("바람과함께사라지다");
            movie.setPrice(10000);

            Album album = new Album();
            album.setArtist("김광석");
            album.setName("가을하늘");
            album.setPrice(5000);

            Book book = new Book();
            book.setAuthor("홍길동");
            book.setIsbn("abcd");
            book.setName("나의라임오렌지나무");
            book.setPrice(12000);

            em.persist(movie);
            em.persist(album);
            em.persist(book);

            System.out.println("movie.getId = " + movie.getId());

            em.flush();
            em.clear();

            System.out.println("movie.getId2 = " + movie.getId());

            Movie findMovie = em.find(Movie.class, movie.getId());
            Album findAlbum = em.find(Album.class, album.getId());
            Book findBook = em.find(Book.class, book.getId());

            System.out.println("findMovie = " + findMovie);
            System.out.println("findAlbum = " + findAlbum);
            System.out.println("findBook = " + findBook);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
