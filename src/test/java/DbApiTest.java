import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DbApiTest {
    private DbApi dbApi = mock(DbApi.class);
    private static final String EX_NAME = "existingName";
    private static final String NOT_EX_NAME = "notExistingName";
    private static final String NEW_LAST_NAME = "newLastName";

    @Test
    public void testResponseHaveColumnNameAndLastName() {
        Map<String, String> response = new HashMap<>();
        response.put("Name", "");
        response.put("Last_name", "");
        when(dbApi.getAccountByName(EX_NAME)).thenReturn(response);
        assertTrue(dbApi.getAccountByName(EX_NAME).containsKey("Name"));
        assertTrue(dbApi.getAccountByName(EX_NAME).containsKey("Last_name"));
    }

    @Test
    public void testResponseIsEmpty() {
        when(dbApi.getAccountByName(NOT_EX_NAME)).thenReturn(new HashMap<>());
        assertTrue(dbApi.getAccountByName(NOT_EX_NAME).isEmpty());
    }

    @Test
    public void testGetAccountWithEmptyName() {
        RuntimeException exception = new RuntimeException("This name is not exist");
        when(dbApi.getAccountByName("")).thenThrow(exception);
    }

    @Test
    public void testGetAccountWithNameIsNull() {
        RuntimeException exception = new RuntimeException("This name is not exist");
        when(dbApi.getAccountByName(null)).thenThrow(exception);
    }

    @Test
    public void testUpdateLastNameWithExistingName() {
        when(dbApi.updateLastNameByName(EX_NAME, NEW_LAST_NAME)).thenReturn(1);
        assertEquals(dbApi.updateLastNameByName(EX_NAME, NEW_LAST_NAME), 1, "Wrong count updated rows");
    }

    @Test
    public void testUpdateLastNameWithNotExistingName() {
        when(dbApi.updateLastNameByName(NOT_EX_NAME, NEW_LAST_NAME)).thenReturn(0);
        assertEquals(dbApi.updateLastNameByName(NOT_EX_NAME, NEW_LAST_NAME), 0, "Wrong count updated rows");
    }
}
