package q3;

// test class for the garden, copied from testBarrier

public class GardenTest{
    static int totalHolesToDig = 5;

    public static void main(String[] args) {
        Garden garden = new Garden();

        Thread mary = new Thread(new Mary(garden));
        Thread benjamin = new Thread(new Benjamin(garden));
        Thread newton = new Thread(new Newton(garden));

        mary.start();
        benjamin.start();
        newton.start();
    }

    static class Newton implements Runnable {
        private final Garden garden;

        public Newton(Garden garden) {
            this.garden = garden;
        }

        @Override
        public void run() {
            while (garden.totalHolesDugByNewton() <= totalHolesToDig) {
                try {
                    garden.startDigging();
                    dig();
                } catch (Exception e) {
                    System.out.println("Error digging");
                } finally  {
                    garden.doneDigging();
                }
            }
        }

        private void dig() throws InterruptedException {
            System.out.println("Digging... \n Total holes dug is: " + garden.totalHolesDugByNewton());
        }
    }

    protected static class Benjamin implements Runnable {
        private final Garden garden;

        public Benjamin(Garden garden) {
            this.garden = garden;
        }

        @Override
        public void run() {
            while (garden.totalHolesSeededByBenjamin() <= totalHolesToDig) {
                try  {
                    garden.startSeeding();
                    plant();
                } catch (Exception e) {
                    System.out.println("Error seeding");
                } finally {
                    garden.doneSeeding();
                }
            }
        }

        private void plant() throws InterruptedException {
            System.out.println("Seeding... \n Total holes seeded is: " + garden.totalHolesSeededByBenjamin());
        }
    }

    /**
     * An implementation of the Mary thread.
     */
    protected static class Mary implements Runnable {
        private final Garden garden;

        public Mary(Garden garden) {
            this.garden = garden;
        }

        @Override
        public void run() {
            while (garden.totalHolesFilledByMary() <= totalHolesToDig) {
                try {
                    garden.startFilling();
                    fill();
                } catch (Exception e) {
                    System.out.println("Error filling");
                } finally {
                    garden.doneFilling();
                }
            }
        }

        private void fill(){
            System.out.println("Filling... \n Total holes filled is: " + garden.totalHolesFilledByMary());
        }
    }

}