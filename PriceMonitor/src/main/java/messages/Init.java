package messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.PriceMonitoringService;

public class Init {
    private PriceMonitoringService service;

    public Init(PriceMonitoringService service){
        this.service = service;
    }

    public PriceMonitoringService getService() {
        return service;
    }

    public void setService(PriceMonitoringService service) {
        this.service = service;
    }

    public Init(){}
}
