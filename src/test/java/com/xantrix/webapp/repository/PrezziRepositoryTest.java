
package com.xantrix.webapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.*;

import javax.transaction.Transactional;

import com.xantrix.webapp.entities.DettListini;
import com.xantrix.webapp.entities.Listini;

import lombok.extern.java.Log;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
 
import org.springframework.test.context.TestPropertySource;
 
@TestPropertySource(locations="classpath:application-list1.properties")
//@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class PrezziRepositoryTest
{
    @Autowired
    private PrezziRepository prezziRepository;
    
    @Autowired
    private ListinoRepository listinoRepository;
    
    String IdList = "100";
    String CodArt = "098049302";
    BigDecimal Prezzo = new BigDecimal( "1.00" );
    
    @Test
    @Order(1)
    public void testInsListino()
    {
    	Listini listinoTest = new Listini(IdList,"Listino Test 100","No");
    	
    	Set<DettListini> dettListini = new HashSet<>();
    	DettListini dettListTest = new DettListini(CodArt,Prezzo, listinoTest);
    	dettListini.add(dettListTest);
    	
    	listinoTest.setDettListini(dettListini);
    	
    	listinoRepository.save(listinoTest);
    	
    	assertThat(listinoRepository
        		.findById(IdList))
    			.isNotEmpty();
    	
    }
    
    @Test
    @Order(2)
	public void testfindByCodArtAndIdList1()
	{
    	BigDecimal price = prezziRepository.SelByCodArtAndList(CodArt, IdList).getPrezzo();

        assertThat(price).isEqualTo(Prezzo);
    }
    
    @Test
    @Transactional
    @Order(3)
	public void testDeletePrezzo()
	{
        
    	prezziRepository.DelRowDettList(CodArt, IdList);
    	
        assertThat(prezziRepository
        		.SelByCodArtAndList(CodArt, IdList))
        		.isNull();
    }

    @Test
    @Order(4)
    public void testDeleteListino() {
        Optional<Listini> listinoTest = listinoRepository.findById(IdList);

        assertThat(listinoTest)
                .as("Il listino con ID %s dovrebbe esistere prima di eliminarlo", IdList)
                .isPresent();

        listinoTest.ifPresent(listino -> {
            listinoRepository.delete(listino);

            assertThat(listinoRepository.findById(IdList))
                    .as("Il listino con ID %s dovrebbe essere stato eliminato", IdList)
                    .isEmpty();

            assertThat(prezziRepository.SelByCodArtAndList(CodArt, IdList))
                    .as("Non dovrebbero esserci prezzi per il codice articolo %s e l'ID lista %s dopo l'eliminazione", CodArt, IdList)
                    .isNull();
        });
    }

}




















