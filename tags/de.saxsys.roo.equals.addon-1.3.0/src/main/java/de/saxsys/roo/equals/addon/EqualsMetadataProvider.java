package de.saxsys.roo.equals.addon;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;

/**
 * @author stefan.ocke
 */
@Component(immediate = true)
@Service
public class EqualsMetadataProvider extends AbstractItdMetadataProvider {

  protected void activate(ComponentContext context) {
    metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier
        .getMetadataIdentiferType(), getProvidesType());
    addMetadataTrigger(new JavaType(RooEquals.class.getName()));
  }

  protected ItdTypeDetailsProvidingMetadataItem getMetadata(String metadataId,
      JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata,
      String itdFilename) {

    // Create the metadata
    return new EqualsMetadata(metadataId, aspectName,
        governorPhysicalTypeMetadata);
  }

  public String getItdUniquenessFilenameSuffix() {
    return "Equals";
  }

  protected String getGovernorPhysicalTypeIdentifier(String metadataId) {
    JavaType javaType = EqualsMetadata.getJavaType(metadataId);
    Path path = EqualsMetadata.getPath(metadataId);
    String physicalTypeId = PhysicalTypeIdentifier.createIdentifier(javaType,
        path);
    return physicalTypeId;
  }

  // returns for example (for a class de.foo.Bar)
  // MID:d.s.r.e.a.EqualsMetadata#SRC_MAIN_JAVA?de.foo.Bar
  protected String createLocalIdentifier(JavaType javaType, Path path) {
    return EqualsMetadata.createIdentifier(javaType, path);
  }

  // returns MID:d.s.r.e.a.EqualsMetadata
  public String getProvidesType() {
    return EqualsMetadata.getMetadataIdentiferType();
  }
}
