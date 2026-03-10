package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {
    @InjectMocks
    CarRepository carRepository;

    @Test
    void testCreateAndFind() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("BMW");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car savedCar = carIterator.next();
        assertEquals(car.getCarId(), savedCar.getCarId());
        assertEquals(car.getCarName(), savedCar.getCarName());
        assertEquals(car.getCarColor(), savedCar.getCarColor());
        assertEquals(car.getCarQuantity(), savedCar.getCarQuantity());
    }

    @Test
    void testCreateKeepsNullIdWhenCarIdIsNull() {
        Car car = new Car();
        car.setCarName("Audi");
        car.setCarColor("White");
        car.setCarQuantity(5);

        Car createdCar = carRepository.create(car);
        assertNull(createdCar.getCarId());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Car> carIterator = carRepository.findAll();
        assertFalse(carIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneCar() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("BMW");
        car1.setCarColor("Black");
        car1.setCarQuantity(10);
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Audi");
        car2.setCarColor("White");
        car2.setCarQuantity(5);
        carRepository.create(car2);

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car savedCar = carIterator.next();
        assertEquals(car1.getCarId(), savedCar.getCarId());
        savedCar = carIterator.next();
        assertEquals(car2.getCarId(), savedCar.getCarId());
        assertFalse(carIterator.hasNext());
    }

    @Test
    void testFindByIdIfFound() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("BMW");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car result = carRepository.findById("car-1");
        assertNotNull(result);
        assertEquals("car-1", result.getCarId());
    }

    @Test
    void testFindByIdIfNotFound() {
        Car result = carRepository.findById("missing");
        assertNull(result);
    }

    @Test
    void testFindByIdIfFoundOnSecondCar() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("BMW");
        car1.setCarColor("Black");
        car1.setCarQuantity(10);
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Audi");
        car2.setCarColor("White");
        car2.setCarQuantity(5);
        carRepository.create(car2);

        Car result = carRepository.findById("car-2");
        assertNotNull(result);
        assertEquals("car-2", result.getCarId());
    }

    @Test
    void testUpdateSuccess() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Old Name");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("New Name");
        updatedCar.setCarColor("White");
        updatedCar.setCarQuantity(20);

        Car result = carRepository.update("car-1", updatedCar);
        assertNotNull(result);
        assertEquals("New Name", result.getCarName());
        assertEquals("White", result.getCarColor());
        assertEquals(20, result.getCarQuantity());
        assertEquals("car-1", result.getCarId());
    }

    @Test
    void testUpdateNotFound() {
        Car updatedCar = new Car();
        updatedCar.setCarName("New Name");
        updatedCar.setCarColor("White");
        updatedCar.setCarQuantity(20);

        Car result = carRepository.update("missing", updatedCar);
        assertNull(result);
    }

    @Test
    void testUpdateSecondCarSuccess() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("BMW");
        car1.setCarColor("Black");
        car1.setCarQuantity(10);
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Audi");
        car2.setCarColor("White");
        car2.setCarQuantity(5);
        carRepository.create(car2);

        Car updatedCar = new Car();
        updatedCar.setCarName("Updated");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(20);

        Car result = carRepository.update("car-2", updatedCar);
        assertNotNull(result);
        assertEquals("Updated", result.getCarName());
    }

    @Test
    void testDeleteSuccess() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("BMW");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);
        carRepository.delete("car-1");

        Iterator<Car> carIterator = carRepository.findAll();
        assertFalse(carIterator.hasNext());
        assertNull(carRepository.findById("car-1"));
    }

    @Test
    void testDeleteNonExistingDoesNothing() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("BMW");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);
        carRepository.delete("missing");

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car remainingCar = carIterator.next();
        assertEquals("car-1", remainingCar.getCarId());
        assertFalse(carIterator.hasNext());
    }
}