package com.invest.ivppad.model.protobuf;// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ppduseraccesstoken.proto

public final class PPDUserAccessTokenProtos {
  private PPDUserAccessTokenProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface PPDUserAccessTokenOrBuilder extends
      // @@protoc_insertion_point(interface_extends:model.PPDUserAccessToken)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required string accessToken = 1;</code>
     */
    boolean hasAccessToken();
    /**
     * <code>required string accessToken = 1;</code>
     */
    java.lang.String getAccessToken();
    /**
     * <code>required string accessToken = 1;</code>
     */
    com.google.protobuf.ByteString
        getAccessTokenBytes();

    /**
     * <code>required int32 expiresIn = 2;</code>
     */
    boolean hasExpiresIn();
    /**
     * <code>required int32 expiresIn = 2;</code>
     */
    int getExpiresIn();

    /**
     * <code>optional string refreshToken = 3;</code>
     */
    boolean hasRefreshToken();
    /**
     * <code>optional string refreshToken = 3;</code>
     */
    java.lang.String getRefreshToken();
    /**
     * <code>optional string refreshToken = 3;</code>
     */
    com.google.protobuf.ByteString
        getRefreshTokenBytes();
  }
  /**
   * Protobuf type {@code model.PPDUserAccessToken}
   */
  public  static final class PPDUserAccessToken extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:model.PPDUserAccessToken)
      PPDUserAccessTokenOrBuilder {
    // Use PPDUserAccessToken.newBuilder() to construct.
    private PPDUserAccessToken(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private PPDUserAccessToken() {
      accessToken_ = "";
      expiresIn_ = 0;
      refreshToken_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private PPDUserAccessToken(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              accessToken_ = bs;
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              expiresIn_ = input.readInt32();
              break;
            }
            case 26: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000004;
              refreshToken_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return PPDUserAccessTokenProtos.internal_static_model_PPDUserAccessToken_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return PPDUserAccessTokenProtos.internal_static_model_PPDUserAccessToken_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              PPDUserAccessTokenProtos.PPDUserAccessToken.class, PPDUserAccessTokenProtos.PPDUserAccessToken.Builder.class);
    }

    private int bitField0_;
    public static final int ACCESSTOKEN_FIELD_NUMBER = 1;
    private volatile java.lang.Object accessToken_;
    /**
     * <code>required string accessToken = 1;</code>
     */
    public boolean hasAccessToken() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required string accessToken = 1;</code>
     */
    public java.lang.String getAccessToken() {
      java.lang.Object ref = accessToken_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          accessToken_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string accessToken = 1;</code>
     */
    public com.google.protobuf.ByteString
        getAccessTokenBytes() {
      java.lang.Object ref = accessToken_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        accessToken_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int EXPIRESIN_FIELD_NUMBER = 2;
    private int expiresIn_;
    /**
     * <code>required int32 expiresIn = 2;</code>
     */
    public boolean hasExpiresIn() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required int32 expiresIn = 2;</code>
     */
    public int getExpiresIn() {
      return expiresIn_;
    }

    public static final int REFRESHTOKEN_FIELD_NUMBER = 3;
    private volatile java.lang.Object refreshToken_;
    /**
     * <code>optional string refreshToken = 3;</code>
     */
    public boolean hasRefreshToken() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional string refreshToken = 3;</code>
     */
    public java.lang.String getRefreshToken() {
      java.lang.Object ref = refreshToken_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          refreshToken_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string refreshToken = 3;</code>
     */
    public com.google.protobuf.ByteString
        getRefreshTokenBytes() {
      java.lang.Object ref = refreshToken_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        refreshToken_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasAccessToken()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasExpiresIn()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, accessToken_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, expiresIn_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, refreshToken_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, accessToken_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, expiresIn_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, refreshToken_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof PPDUserAccessTokenProtos.PPDUserAccessToken)) {
        return super.equals(obj);
      }
      PPDUserAccessTokenProtos.PPDUserAccessToken other = (PPDUserAccessTokenProtos.PPDUserAccessToken) obj;

      boolean result = true;
      result = result && (hasAccessToken() == other.hasAccessToken());
      if (hasAccessToken()) {
        result = result && getAccessToken()
            .equals(other.getAccessToken());
      }
      result = result && (hasExpiresIn() == other.hasExpiresIn());
      if (hasExpiresIn()) {
        result = result && (getExpiresIn()
            == other.getExpiresIn());
      }
      result = result && (hasRefreshToken() == other.hasRefreshToken());
      if (hasRefreshToken()) {
        result = result && getRefreshToken()
            .equals(other.getRefreshToken());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasAccessToken()) {
        hash = (37 * hash) + ACCESSTOKEN_FIELD_NUMBER;
        hash = (53 * hash) + getAccessToken().hashCode();
      }
      if (hasExpiresIn()) {
        hash = (37 * hash) + EXPIRESIN_FIELD_NUMBER;
        hash = (53 * hash) + getExpiresIn();
      }
      if (hasRefreshToken()) {
        hash = (37 * hash) + REFRESHTOKEN_FIELD_NUMBER;
        hash = (53 * hash) + getRefreshToken().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static PPDUserAccessTokenProtos.PPDUserAccessToken parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(PPDUserAccessTokenProtos.PPDUserAccessToken prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code model.PPDUserAccessToken}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:model.PPDUserAccessToken)
        PPDUserAccessTokenProtos.PPDUserAccessTokenOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return PPDUserAccessTokenProtos.internal_static_model_PPDUserAccessToken_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return PPDUserAccessTokenProtos.internal_static_model_PPDUserAccessToken_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                PPDUserAccessTokenProtos.PPDUserAccessToken.class, PPDUserAccessTokenProtos.PPDUserAccessToken.Builder.class);
      }

      // Construct using PPDUserAccessTokenProtos.PPDUserAccessToken.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        accessToken_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        expiresIn_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        refreshToken_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return PPDUserAccessTokenProtos.internal_static_model_PPDUserAccessToken_descriptor;
      }

      public PPDUserAccessTokenProtos.PPDUserAccessToken getDefaultInstanceForType() {
        return PPDUserAccessTokenProtos.PPDUserAccessToken.getDefaultInstance();
      }

      public PPDUserAccessTokenProtos.PPDUserAccessToken build() {
        PPDUserAccessTokenProtos.PPDUserAccessToken result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public PPDUserAccessTokenProtos.PPDUserAccessToken buildPartial() {
        PPDUserAccessTokenProtos.PPDUserAccessToken result = new PPDUserAccessTokenProtos.PPDUserAccessToken(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.accessToken_ = accessToken_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.expiresIn_ = expiresIn_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.refreshToken_ = refreshToken_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof PPDUserAccessTokenProtos.PPDUserAccessToken) {
          return mergeFrom((PPDUserAccessTokenProtos.PPDUserAccessToken)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(PPDUserAccessTokenProtos.PPDUserAccessToken other) {
        if (other == PPDUserAccessTokenProtos.PPDUserAccessToken.getDefaultInstance()) return this;
        if (other.hasAccessToken()) {
          bitField0_ |= 0x00000001;
          accessToken_ = other.accessToken_;
          onChanged();
        }
        if (other.hasExpiresIn()) {
          setExpiresIn(other.getExpiresIn());
        }
        if (other.hasRefreshToken()) {
          bitField0_ |= 0x00000004;
          refreshToken_ = other.refreshToken_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        if (!hasAccessToken()) {
          return false;
        }
        if (!hasExpiresIn()) {
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        PPDUserAccessTokenProtos.PPDUserAccessToken parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (PPDUserAccessTokenProtos.PPDUserAccessToken) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object accessToken_ = "";
      /**
       * <code>required string accessToken = 1;</code>
       */
      public boolean hasAccessToken() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required string accessToken = 1;</code>
       */
      public java.lang.String getAccessToken() {
        java.lang.Object ref = accessToken_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            accessToken_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string accessToken = 1;</code>
       */
      public com.google.protobuf.ByteString
          getAccessTokenBytes() {
        java.lang.Object ref = accessToken_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          accessToken_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string accessToken = 1;</code>
       */
      public Builder setAccessToken(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        accessToken_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string accessToken = 1;</code>
       */
      public Builder clearAccessToken() {
        bitField0_ = (bitField0_ & ~0x00000001);
        accessToken_ = getDefaultInstance().getAccessToken();
        onChanged();
        return this;
      }
      /**
       * <code>required string accessToken = 1;</code>
       */
      public Builder setAccessTokenBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        accessToken_ = value;
        onChanged();
        return this;
      }

      private int expiresIn_ ;
      /**
       * <code>required int32 expiresIn = 2;</code>
       */
      public boolean hasExpiresIn() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required int32 expiresIn = 2;</code>
       */
      public int getExpiresIn() {
        return expiresIn_;
      }
      /**
       * <code>required int32 expiresIn = 2;</code>
       */
      public Builder setExpiresIn(int value) {
        bitField0_ |= 0x00000002;
        expiresIn_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int32 expiresIn = 2;</code>
       */
      public Builder clearExpiresIn() {
        bitField0_ = (bitField0_ & ~0x00000002);
        expiresIn_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object refreshToken_ = "";
      /**
       * <code>optional string refreshToken = 3;</code>
       */
      public boolean hasRefreshToken() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional string refreshToken = 3;</code>
       */
      public java.lang.String getRefreshToken() {
        java.lang.Object ref = refreshToken_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            refreshToken_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string refreshToken = 3;</code>
       */
      public com.google.protobuf.ByteString
          getRefreshTokenBytes() {
        java.lang.Object ref = refreshToken_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          refreshToken_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string refreshToken = 3;</code>
       */
      public Builder setRefreshToken(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        refreshToken_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string refreshToken = 3;</code>
       */
      public Builder clearRefreshToken() {
        bitField0_ = (bitField0_ & ~0x00000004);
        refreshToken_ = getDefaultInstance().getRefreshToken();
        onChanged();
        return this;
      }
      /**
       * <code>optional string refreshToken = 3;</code>
       */
      public Builder setRefreshTokenBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        refreshToken_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:model.PPDUserAccessToken)
    }

    // @@protoc_insertion_point(class_scope:model.PPDUserAccessToken)
    private static final PPDUserAccessTokenProtos.PPDUserAccessToken DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new PPDUserAccessTokenProtos.PPDUserAccessToken();
    }

    public static PPDUserAccessTokenProtos.PPDUserAccessToken getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<PPDUserAccessToken>
        PARSER = new com.google.protobuf.AbstractParser<PPDUserAccessToken>() {
      public PPDUserAccessToken parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new PPDUserAccessToken(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<PPDUserAccessToken> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<PPDUserAccessToken> getParserForType() {
      return PARSER;
    }

    public PPDUserAccessTokenProtos.PPDUserAccessToken getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_model_PPDUserAccessToken_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_model_PPDUserAccessToken_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\030ppduseraccesstoken.proto\022\005model\"R\n\022PPD" +
      "UserAccessToken\022\023\n\013accessToken\030\001 \002(\t\022\021\n\t" +
      "expiresIn\030\002 \002(\005\022\024\n\014refreshToken\030\003 \001(\tB\034\n" +
      "\000B\030PPDUserAccessTokenProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_model_PPDUserAccessToken_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_model_PPDUserAccessToken_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_model_PPDUserAccessToken_descriptor,
        new java.lang.String[] { "AccessToken", "ExpiresIn", "RefreshToken", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
