package kz.one_c_rating.osgi;

import kz.gov.pki.osgi.layer.api.ModuleService;
import org.json.JSONObject;
import org.osgi.service.log.LogService;
import java.security.Provider;

public class ModuleServiceImpl implements ModuleService {

    LogService logService;
    Provider provider;

    @Override
    public String process(String jsonString, String headers) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String uuid = jsonObject.optString("uuid");
        String name = jsonObject.getString("name");

        if(logService != null){
            logService.log(LogService.LOG_INFO, "Processing request..");
            if(provider != null){
                logService.log(LogService.LOG_INFO, "Available provider: " + provider.getName());
            }
        }

        SampleResult result = new SampleResult(name);
        return ((JSONObject) JSONObject.wrap(result)).toString();
    }

    public void setLogService(LogService logService){
        this.logService = logService;
    }

    public void setProvider(Provider provider){
        this.provider = provider;
    }
}
