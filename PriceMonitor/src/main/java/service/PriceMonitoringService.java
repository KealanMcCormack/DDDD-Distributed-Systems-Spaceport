package service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import messages.Product;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class PriceMonitoringService {
    RestTemplate restTemplate;

    public void priceChange(Product product){
        //restTemplate.getForObject("http://localhost:8081/inventory/" + product.getName(), )
        if(product.getAmount() > 0){
            decreasePrice();
        }else{
            increasePrice();
        }
    }

    private void decreasePrice(){

    }

    private void increasePrice(){

    }
}
