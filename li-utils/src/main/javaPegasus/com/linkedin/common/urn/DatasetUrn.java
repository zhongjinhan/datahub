package com.linkedin.common.urn;

import com.linkedin.common.FabricType;
import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;
import java.net.URISyntaxException;


public final class DatasetUrn extends Urn {

  public static final String ENTITY_TYPE = "dataset";

  private final DataPlatformUrn _platform;
  private final String _datasetName;
  private final String _layer;

  public DatasetUrn(DataPlatformUrn platform, String name, String layer) {
    super(ENTITY_TYPE, TupleKey.create(platform, name, layer));
    this._platform = platform;
    this._datasetName = name;
    this._layer = layer;
  }

  public DataPlatformUrn getPlatformEntity() {
    return _platform;
  }

  public String getDatasetNameEntity() {
    return _datasetName;
  }

  public String getLayerEntity() {
    return _layer;
  }
  public static DatasetUrn createFromString(String rawUrn) throws URISyntaxException {
    return createFromUrn(Urn.createFromString(rawUrn));
  }

  public static DatasetUrn createFromUrn(Urn urn) throws URISyntaxException {
    if (!"li".equals(urn.getNamespace())) {
      throw new URISyntaxException(urn.toString(), "Urn namespace type should be 'li'.");
    } else if (!ENTITY_TYPE.equals(urn.getEntityType())) {
      throw new URISyntaxException(urn.toString(), "Urn entity type should be 'dataset'.");
    } else {
      TupleKey key = urn.getEntityKey();
      if (key.size() != 3) {
        throw new URISyntaxException(urn.toString(), "Invalid number of keys.");
      } else {
        try {
          return new DatasetUrn((DataPlatformUrn) key.getAs(0, DataPlatformUrn.class),
              (String) key.getAs(1, String.class), (String) key.getAs(2, String.class));
        } catch (Exception var3) {
          throw new URISyntaxException(urn.toString(), "Invalid URN Parameter: '" + var3.getMessage());
        }
      }
    }
  }

  public static DatasetUrn deserialize(String rawUrn) throws URISyntaxException {
    return createFromString(rawUrn);
  }

  static {
    Custom.initializeCustomClass(DataPlatformUrn.class);
    Custom.initializeCustomClass(DatasetUrn.class);
    Custom.initializeCustomClass(FabricType.class);
    Custom.registerCoercer(new DirectCoercer<DatasetUrn>() {
      public Object coerceInput(DatasetUrn object) throws ClassCastException {
        return object.toString();
      }

      public DatasetUrn coerceOutput(Object object) throws TemplateOutputCastException {
        try {
          return DatasetUrn.createFromString((String) object);
        } catch (URISyntaxException e) {
          throw new TemplateOutputCastException("Invalid URN syntax: " + e.getMessage(), e);
        }
      }
    }, DatasetUrn.class);
  }
}
