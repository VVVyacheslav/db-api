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
    private static final RuntimeException EXCEPTION = new RuntimeException("This name is not exist");

    @Test
    public void testResponseHaveColumnNameAndLastName() {
        Map<String, String> response = new HashMap<>();
        response.put("Name", "");
        response.put("Last_name", "");
        when(dbApi.getAccountByName(EX_NAME)).thenReturn(response);
        assertTrue(dbApi.getAccountByName(EX_NAME).containsKey("Name"), "Column 'Name' is missing");
        assertTrue(dbApi.getAccountByName(EX_NAME).containsKey("Last_name"), "Column 'Last_name' is missing");
        verify(dbApi, atLeast(2)).getAccountByName(EX_NAME);
    }

    @Test
    public void testResponseIsEmpty() {
        when(dbApi.getAccountByName(NOT_EX_NAME)).thenReturn(new HashMap<>());
        assertTrue(dbApi.getAccountByName(NOT_EX_NAME).isEmpty(), "Response is not empty");
        verify(dbApi, atLeastOnce()).getAccountByName(NOT_EX_NAME);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetAccountWithEmptyName() {
        when(dbApi.getAccountByName("")).thenThrow(EXCEPTION);
        dbApi.getAccountByName("");
        verify(dbApi, atLeastOnce()).getAccountByName("");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetAccountWithNameIsNull() {
        when(dbApi.getAccountByName(null)).thenThrow(EXCEPTION);
        dbApi.getAccountByName(null);
        verify(dbApi, atLeastOnce()).getAccountByName(null);
    }

    @Test
    public void testUpdateLastNameWithExistingName() {
        when(dbApi.updateLastNameByName(EX_NAME, NEW_LAST_NAME)).thenReturn(1);
        assertEquals(dbApi.updateLastNameByName(EX_NAME, NEW_LAST_NAME), 1, "Wrong count updated rows");
        verify(dbApi, atLeastOnce()).updateLastNameByName(EX_NAME, NEW_LAST_NAME);
    }

    @Test
    public void testUpdateLastNameWithNotExistingName() {
        when(dbApi.updateLastNameByName(NOT_EX_NAME, NEW_LAST_NAME)).thenReturn(0);
        assertEquals(dbApi.updateLastNameByName(NOT_EX_NAME, NEW_LAST_NAME), 0, "Wrong count updated rows");
        verify(dbApi, atLeastOnce()).updateLastNameByName(NOT_EX_NAME, NEW_LAST_NAME);
    }
}
