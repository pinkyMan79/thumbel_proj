package one.terenin.security;

import one.terenin.security.propertysource.JWTPropertySource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MyPasswordEncoderTest {

    @Mock
    JWTPropertySource propertySource;

    @InjectMocks
    MyPasswordEncoder myPasswordEncoder;

    @Test
    public void encodeTest() {
        when(propertySource.getPasswordEncoderSecret()).thenReturn("TestSecret");
        when(propertySource.getPasswordEncoderIteration()).thenReturn(10000);
        when(propertySource.getPasswordEncoderKeyLength()).thenReturn(512);

        String rawPassword = "TestRawPassword";
        String encodedPassword = myPasswordEncoder.encode(rawPassword);
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    public void matchTest() {
        when(propertySource.getPasswordEncoderSecret()).thenReturn("TestSecret");
        when(propertySource.getPasswordEncoderIteration()).thenReturn(10000);
        when(propertySource.getPasswordEncoderKeyLength()).thenReturn(512);

        String rawPassword = "TestRawPassword";
        String encodedPassword = myPasswordEncoder.encode(rawPassword);
        assertTrue(myPasswordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void unMatchTest() {
        when(propertySource.getPasswordEncoderSecret()).thenReturn("TestSecret");
        when(propertySource.getPasswordEncoderIteration()).thenReturn(10000);
        when(propertySource.getPasswordEncoderKeyLength()).thenReturn(512);

        String rawPassword = "TestRawPassword";
        String encodedPassword = myPasswordEncoder.encode(rawPassword);
        assertFalse(myPasswordEncoder.matches("WrongRawPassword", encodedPassword));
    }

}