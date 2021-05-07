package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;


public class GT4500Test {
  private GT4500 ship;
  private TorpedoStore mockPrimary;
  private TorpedoStore mockSecondary;

  @BeforeEach
  public void init(){
    mockPrimary = mock(TorpedoStore.class);
    mockSecondary = mock(TorpedoStore.class); 
    this.ship = new GT4500();
    this.ship.setTorpedoStores(mockPrimary, mockSecondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireAfterSecondary() {
    TorpedoStore m_mockPrimary = mock(TorpedoStore.class);
    TorpedoStore m_mockSecondary = mock(TorpedoStore.class);
    GT4500 m_ship = new GT4500();
    m_ship.setTorpedoStores(m_mockPrimary, m_mockSecondary);

    when(m_mockPrimary.isEmpty()).thenReturn(false);
    when(m_mockPrimary.fire(1)).thenReturn(true);
    when(m_mockSecondary.isEmpty()).thenReturn(false);
    when(m_mockSecondary.fire(1)).thenReturn(true);

    InOrder inOrder = inOrder(m_mockPrimary, m_mockSecondary);
    
    // 1. primary, 2. secondary
    m_ship.fireTorpedo(FiringMode.SINGLE);
    m_ship.fireTorpedo(FiringMode.SINGLE);

    inOrder.verify(m_mockPrimary).fire(1);
    inOrder.verify(m_mockSecondary).fire(1);

    verify(m_mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireAfterPrimary() {
    TorpedoStore m_mockPrimary = mock(TorpedoStore.class);
    TorpedoStore m_mockSecondary = mock(TorpedoStore.class);
    GT4500 m_ship = new GT4500();
    m_ship.setTorpedoStores(m_mockPrimary, m_mockSecondary);

    when(m_mockPrimary.isEmpty()).thenReturn(false);
    when(m_mockPrimary.fire(1)).thenReturn(true);
    when(m_mockSecondary.isEmpty()).thenReturn(false);
    when(m_mockSecondary.fire(1)).thenReturn(true);

    InOrder inOrder = inOrder(m_mockPrimary, m_mockSecondary);

    m_ship.fireTorpedo(FiringMode.SINGLE);
    m_ship.fireTorpedo(FiringMode.SINGLE);
    m_ship.fireTorpedo(FiringMode.SINGLE);

    // 1. primary, 2. secondary, 3. primary
    inOrder.verify(m_mockPrimary).fire(1);
    inOrder.verify(m_mockSecondary).fire(1);
    inOrder.verify(m_mockPrimary).fire(1);

    verify(m_mockPrimary, times(2)).fire(1);
    verify(m_mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireWithEmptyPrimary() {
    TorpedoStore m_mockPrimary = mock(TorpedoStore.class);
    TorpedoStore m_mockSecondary = mock(TorpedoStore.class);
    GT4500 m_ship = new GT4500();
    m_ship.setTorpedoStores(m_mockPrimary, m_mockSecondary);

    when(m_mockPrimary.isEmpty()).thenReturn(true);
    when(m_mockSecondary.isEmpty()).thenReturn(false);
    when(m_mockSecondary.fire(1)).thenReturn(true);
    when(m_mockPrimary.fire(1)).thenReturn(true);

    m_ship.fireTorpedo(FiringMode.SINGLE);

    // primary-t kellett volna, de a secondary-t használta
    verify(m_mockSecondary, times(1)).fire(1);
  }

  @Test
  public void FireWithEmptySecondary() {
    TorpedoStore m_mockPrimary = mock(TorpedoStore.class);
    TorpedoStore m_mockSecondary = mock(TorpedoStore.class);
    GT4500 m_ship = new GT4500();
    m_ship.setTorpedoStores(m_mockPrimary, m_mockSecondary);

    when(m_mockPrimary.isEmpty()).thenReturn(false);
    when(m_mockSecondary.isEmpty()).thenReturn(true);
    when(m_mockPrimary.fire(1)).thenReturn(true);
    when(m_mockSecondary.fire(1)).thenReturn(true);

    m_ship.fireTorpedo(FiringMode.SINGLE);
    m_ship.fireTorpedo(FiringMode.SINGLE);

    // mindkétszer a primary-val lőtt
    verify(m_mockSecondary, times(0)).fire(1);
    verify(m_mockPrimary, times(2)).fire(1);
  }

  @Test
  public void falseFire() {
    TorpedoStore m_mockPrimary = mock(TorpedoStore.class);
    TorpedoStore m_mockSecondary = mock(TorpedoStore.class);
    GT4500 m_ship = new GT4500();
    m_ship.setTorpedoStores(m_mockPrimary, m_mockSecondary);

    when(m_mockPrimary.isEmpty()).thenReturn(false);
    when(m_mockSecondary.isEmpty()).thenReturn(false);
    when(m_mockPrimary.fire(1)).thenReturn(false);
    when(m_mockSecondary.fire(1)).thenReturn(true);

    boolean success = m_ship.fireTorpedo(FiringMode.SINGLE);

    // nem sikerült a lövés
    assertEquals( false, success);

    // meghívta az elsőt, de a másodikat már nem
    verify(m_mockPrimary, times(1)).fire(1);
    verify(m_mockSecondary, times(0)).fire(1);
  }


  @Test
  public void falseAllFire() {
    TorpedoStore m_mockPrimary = mock(TorpedoStore.class);
    TorpedoStore m_mockSecondary = mock(TorpedoStore.class);
    GT4500 m_ship = new GT4500();
    m_ship.setTorpedoStores(m_mockPrimary, m_mockSecondary);

    when(m_mockPrimary.isEmpty()).thenReturn(false);
    when(m_mockSecondary.isEmpty()).thenReturn(false);
    when(m_mockPrimary.fire(1)).thenReturn(true);
    when(m_mockSecondary.fire(1)).thenReturn(false);

    boolean success = m_ship.fireTorpedo(FiringMode.ALL);

    //sikerült a lövés
    assertEquals( true, success);

    when(m_mockPrimary.fire(1)).thenReturn(false);
    when(m_mockSecondary.fire(1)).thenReturn(true);

    success = m_ship.fireTorpedo(FiringMode.ALL);

    //sikerült a lövés
    assertEquals( true, success);

    when(m_mockPrimary.fire(1)).thenReturn(false);
    when(m_mockSecondary.fire(1)).thenReturn(false);

    success = m_ship.fireTorpedo(FiringMode.ALL);

    //nem sikerült a lövés
    assertEquals( false, success);
  }

  @Test
  public void EmptySingleFire() {
    TorpedoStore m_mockPrimary = mock(TorpedoStore.class);
    TorpedoStore m_mockSecondary = mock(TorpedoStore.class);
    GT4500 m_ship = new GT4500();
    m_ship.setTorpedoStores(m_mockPrimary, m_mockSecondary);

    when(m_mockPrimary.isEmpty()).thenReturn(true);
    when(m_mockSecondary.isEmpty()).thenReturn(true);

    boolean success = m_ship.fireTorpedo(FiringMode.SINGLE);

    verify(m_mockPrimary, times(0)).fire(1);
    verify(m_mockSecondary, times(0)).fire(1);
    assertEquals( false, success);

    when(m_mockPrimary.isEmpty()).thenReturn(false);
    m_ship.fireTorpedo(FiringMode.SINGLE);
    when(m_mockPrimary.isEmpty()).thenReturn(true);

    success = m_ship.fireTorpedo(FiringMode.SINGLE);

    verify(m_mockPrimary, times(1)).fire(1);
    verify(m_mockSecondary, times(0)).fire(1);
    assertEquals( false, success);
  }

  @Test
  public void EmptyAllFire() {
    TorpedoStore m_mockPrimary = mock(TorpedoStore.class);
    TorpedoStore m_mockSecondary = mock(TorpedoStore.class);
    GT4500 m_ship = new GT4500();
    m_ship.setTorpedoStores(m_mockPrimary, m_mockSecondary);

    when(m_mockPrimary.isEmpty()).thenReturn(true);
    when(m_mockSecondary.isEmpty()).thenReturn(true);

    boolean success = m_ship.fireTorpedo(FiringMode.ALL);
    verify(m_mockPrimary, times(0)).fire(1);
    verify(m_mockSecondary, times(0)).fire(1);
    assertEquals( false, success);
  }
}

