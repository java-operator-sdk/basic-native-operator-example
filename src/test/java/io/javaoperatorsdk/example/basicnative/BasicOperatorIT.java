package io.javaoperatorsdk.example.basicnative;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.CustomResourceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

@QuarkusTest
class BasicOperatorIT {

    private static final Logger logger = LoggerFactory.getLogger(BasicOperatorIT.class);

    public static final String CRD_YAML = "/k8s/crd.yaml";
    public static final String TEST_OBJECT_YAML = "/k8s/test_object.yaml";

    KubernetesClient k8sClient = new DefaultKubernetesClient();

    @BeforeAll
    static void setupCRD() {
        try (final KubernetesClient k8s = new DefaultKubernetesClient()) {
            CustomResourceDefinition crd = k8s.apiextensions().v1beta1()
                    .customResourceDefinitions()
                    .load(BasicOperatorIT.class.getResourceAsStream(CRD_YAML))
                    .get();

            k8s.apiextensions().v1beta1().customResourceDefinitions().create(crd);
            logger.info("CRD registered.");
        }
    }

    @AfterAll
    static void deleteCRD() {
        try (final KubernetesClient k8s = new DefaultKubernetesClient()) {
            CustomResourceDefinition crd = k8s.apiextensions().v1beta1()
                    .customResourceDefinitions()
                    .load(BasicOperatorIT.class.getResourceAsStream(CRD_YAML))
                    .get();

            k8s.apiextensions().v1beta1().customResourceDefinitions().delete(crd);
        }
        logger.info("CRD unregistered.");
    }

    @Test
    void createResource() {
        CustomResourceDefinition crd = k8sClient.apiextensions().v1beta1()
                .customResourceDefinitions()
                .load(BasicOperatorIT.class.getResourceAsStream(CRD_YAML))
                .get();

        MixedOperation<CustomService, CustomServiceList, CustomServiceDoneable, Resource<CustomService, CustomServiceDoneable>> customServiceClient = null;
        CustomResourceDefinitionContext context = CustomResourceDefinitionContext.fromCrd(crd);

        customServiceClient = k8sClient.customResources(context, CustomService.class, CustomServiceList.class, CustomServiceDoneable.class);

        Resource<CustomService, CustomServiceDoneable> resource = customServiceClient.load(BasicOperatorIT.class.getResourceAsStream(TEST_OBJECT_YAML));
        customServiceClient.inNamespace("default").create(resource.get());

        CustomServiceList serviceList = customServiceClient.list();

        Assertions.assertEquals(1, serviceList.getItems().size());

        Assertions.assertEquals(resource.get().getSpec().getName(), serviceList.getItems().stream().findFirst().get().getSpec().getName());
    }

}