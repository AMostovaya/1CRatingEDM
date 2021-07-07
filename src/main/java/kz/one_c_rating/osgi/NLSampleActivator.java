package kz.one_c_rating.osgi;

import kz.gov.pki.osgi.layer.api.ModuleService;
import kz.gov.pki.osgi.layer.api.NCALayerService;
import org.osgi.framework.*;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import java.util.Hashtable;

public class NLSampleActivator implements BundleActivator
{
    LogService logService = null;
    NCALayerService ncaLayerService;

    @Override
    public void start(BundleContext context) throws Exception {
        ModuleServiceImpl moduleService = new ModuleServiceImpl();

        //Достаем сервис логирования
        getLogService(context, moduleService);

        //Достаем сервис NCALayerService
        getNCALayerService(context, moduleService);

        //Регистрация сервиса kz.sample.osgi.NLSampleModule
        registerService(context, moduleService);
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

    private void registerService(BundleContext context, ModuleServiceImpl moduleService) {
        Hashtable<String, String> props = new Hashtable<String, String>();

        //имя сервиса - kz.sample.osgi.NLSampleModule
        //Запрос к NcaLayer должен содержать параметр "module", имя сервиса, к которому идет обращение. Например:
        //{
        //  module: "kz.sample.osgi.NLSampleModule",
        //  name: "UserName",
        //  ...
        //}
        props.put("module", "kz.sample.osgi.NLSampleModule");

        ServiceRegistration serviceRegistration = context.registerService(ModuleService.class.getName().toString(), moduleService, props);
    }

    private void getLogService(BundleContext context, ModuleServiceImpl moduleService) throws InvalidSyntaxException {
        //Ищем сервис
        String logServiceFilter = "(objectClass=" + LogService.class.getName() + ")";
        ServiceTracker<LogService, LogService> logService_tracker = new ServiceTracker(context, context.createFilter(logServiceFilter), null);
        logService_tracker.open();
        logService = logService_tracker.getService();

        //Если нужный сервис ещё не зарегистрирован, ServiceListener дождется его и запустит указанный обработчик.
        if (logService == null) {
            context.addServiceListener((e) -> {
                if (e.getType() == ServiceEvent.REGISTERED) {
                    logService = logService_tracker.getService((ServiceReference<LogService>) e.getServiceReference());
                    moduleService.setLogService(logService);
                }
            }, logServiceFilter);
        } else {
            moduleService.setLogService(logService);
        }
    }

    private void getNCALayerService(BundleContext context, ModuleServiceImpl moduleService) throws InvalidSyntaxException {
        //Ищем сервис
        String filter = "(objectClass=" + NCALayerService.class.getName() + ")";
        ServiceTracker<NCALayerService, NCALayerService> ncaLayerService_tracker = new ServiceTracker(context, context.createFilter(filter), null);
        ncaLayerService_tracker.open();
        ncaLayerService = ncaLayerService_tracker.getService();

        //Если нужный сервис ещё не зарегистрирован, ServiceListener дождется его и запустит указанный обработчик.
        if (ncaLayerService == null) {
            context.addServiceListener((e) -> {
                if (e.getType() == ServiceEvent.REGISTERED) {
                    ncaLayerService = ncaLayerService_tracker.getService((ServiceReference<NCALayerService>) e.getServiceReference());

                    moduleService.setProvider(ncaLayerService.getProvider());
                }
            }, filter);
        } else {
            moduleService.setProvider(ncaLayerService.getProvider());
        }
    }
}
