package hipravin.samples.jpaid.persist.sqlinjection;

import org.junit.jupiter.api.Test;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JdbcPersonRepositoryIT {
    @Autowired
    JdbcVulnerablePersonRepository jdbcVulnerablePersonRepository;

    @Test
    void testSampleFind() {
        List<PersonEntity> persons = jdbcVulnerablePersonRepository.findByName("John Doe");
        assertNotNull(persons);
        assertEquals(1, persons.size());
        PersonEntity john = persons.get(0);
        assertEquals("John Doe", john.getName());
    }

    @Test
    void testInjectionOr11() {
        List<PersonEntity> persons = jdbcVulnerablePersonRepository.findByName("1' or 1=1 or name = '");
        assertNotNull(persons);
        assertEquals(2, persons.size());

    }

    @Test
    void testInjectionTruncate() {
        List<PersonEntity> persons = jdbcVulnerablePersonRepository.findByName("1'; truncate person; select '");
        assertNotNull(persons);
        assertEquals(2, persons.size());
    }

    @Test
    void showcaseEsapi() {
        Codec pgCodec = new OracleCodec();
        String name = "O'Reilly";

        String nameEncoded = ESAPI.encoder().encodeForSQL(new OracleCodec(), name);
        String query = "select from person where name = '" + nameEncoded + "'";

        System.out.println(query);
    }
}