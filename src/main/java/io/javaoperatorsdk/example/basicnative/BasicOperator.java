package io.javaoperatorsdk.example.basicnative;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;


@QuarkusMain
public class BasicOperator {

    public static void main(String[] args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {

        private static final Logger LOG = Logger.getLogger(MyApp.class);

        @Override
        public int run(String... args) {
            LOG.info("Hello");
            KubernetesClient client = new DefaultKubernetesClient();
            Operator operator = new Operator(client);
            operator.registerController(new CustomServiceController(client));
            Quarkus.waitForExit();
            return 0;
        }
    }
}
