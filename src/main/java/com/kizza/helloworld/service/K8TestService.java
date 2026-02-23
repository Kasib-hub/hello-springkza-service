package com.kizza.helloworld.service;

/*
Trying out some handy kubernetes operations from spring boot
 */

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.LocalPortForward;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CountDownLatch;

@Slf4j
@AllArgsConstructor
@Service
public class K8TestService implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Config kubeConfig = Config.fromKubeconfig(new File(
                System.getenv("HOME") + "/.kube/config")
        );

        log.info("Initializing kube client...");
        try (KubernetesClient kubeClient = new KubernetesClientBuilder().withConfig(kubeConfig).build()) {
            // POD EVENTS IN NS
            log.info("getting events in last hour..");
            kubeClient.v1().events().inNamespace("default").list().getItems()
                    .stream()
                    .filter(e -> e.getInvolvedObject().getKind().equals("Deployment")
                            || e.getInvolvedObject().getKind().equals("HorizontalPodScaler")
                            || e.getInvolvedObject().getKind().equals("ReplicaSet")
                            || e.getInvolvedObject().getKind().equals("Pod"))
                    .forEach(e -> System.out.println(
                            e.getLastTimestamp() + " | " +
                                    e.getInvolvedObject().getKind() + " | " +
                                    e.getInvolvedObject().getName() + " | " +
                                    e.getReason() + ": " +
                                    e.getMessage()
                    ));

            // METRICS
            log.info("showing metrics...");
            kubeClient.top().pods().inNamespace("default").metrics().getItems()
                    .forEach(e -> System.out.println(
                            e.getMetadata().getName() + " | " +
                                    e.getContainers().get(0).getUsage()
                    ));

            // PORT FORWARDING
            // Hold a strong, explicit reference — don't let this go out of scope
            LocalPortForward portForward = kubeClient.pods()
                    .inNamespace("default")
                    .withName("helloworld-deployment-6b7567bb97-xl95p")
                    .portForward(5005, 5005); // remotePort, localPort

            log.info("Port forward active: localhost:{}", portForward.getLocalPort());
            log.info("Alive: {}", portForward.isAlive());

            // Use a latch on a non-daemon thread to block indefinitely
            CountDownLatch latch = new CountDownLatch(1);

            // Shutdown hook so Ctrl+C closes cleanly
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    log.info("Closing port forward...");
                    portForward.close();
                    kubeClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }));

            latch.await(); // Blocks the main thread on a non-daemon thread
        } catch (Exception e) {
            log.error("Error doing kube operation, {}", e.getMessage());
        }
    }
}
