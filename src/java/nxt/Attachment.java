package nxt;

import nxt.util.Convert;
import nxt.util.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public interface Attachment {

    public int getSize();
    public byte[] getBytes();
    public JSONStreamAware getJSON();

    Transaction.Type getTransactionType();


    public static class MessagingArbitraryMessage implements Attachment, Serializable {

        static final long serialVersionUID = 0;

        private final byte[] message;

        public MessagingArbitraryMessage(byte[] message) {

            this.message = message;

        }

        @Override
        public int getSize() {
            return 4 + message.length;
        }

        @Override
        public byte[] getBytes() {

            ByteBuffer buffer = ByteBuffer.allocate(getSize());
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(message.length);
            buffer.put(message);

            return buffer.array();

        }

        @Override
        public JSONStreamAware getJSON() {

            JSONObject attachment = new JSONObject();
            attachment.put("message", Convert.convert(message));

            return attachment;

        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.Messaging.ARBITRARY_MESSAGE;
        }

        public byte[] getMessage() {
            return message;
        }
    }

    public static class MessagingAliasAssignment implements Attachment, Serializable {

        static final long serialVersionUID = 0;

        private final String aliasName;
        private final String aliasURI;

        public MessagingAliasAssignment(String aliasName, String aliasURI) {

            this.aliasName = aliasName.trim().intern();
            this.aliasURI = aliasURI.trim().intern();

        }

        @Override
        public int getSize() {
            try {
                return 1 + aliasName.getBytes("UTF-8").length + 2 + aliasURI.getBytes("UTF-8").length;
            } catch (RuntimeException|UnsupportedEncodingException e) {
                Logger.logMessage("Error in getBytes", e);
                return 0;
            }
        }

        @Override
        public byte[] getBytes() {

            try {

                byte[] alias = this.aliasName.getBytes("UTF-8");
                byte[] uri = this.aliasURI.getBytes("UTF-8");

                ByteBuffer buffer = ByteBuffer.allocate(1 + alias.length + 2 + uri.length);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.put((byte)alias.length);
                buffer.put(alias);
                buffer.putShort((short)uri.length);
                buffer.put(uri);

                return buffer.array();

            } catch (RuntimeException|UnsupportedEncodingException e) {
                Logger.logMessage("Error in getBytes", e);
                return null;

            }

        }

        @Override
        public JSONStreamAware getJSON() {

            JSONObject attachment = new JSONObject();
            attachment.put("alias", aliasName);
            attachment.put("uri", aliasURI);

            return attachment;

        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.Messaging.ALIAS_ASSIGNMENT;
        }

        public String getAliasName() {
            return aliasName;
        }

        public String getAliasURI() {
            return aliasURI;
        }
    }

    public static class MessagingPollCreation implements Attachment, Serializable {

        static final long serialVersionUID = 0;

        public MessagingPollCreation() {

        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public byte[] getBytes() {

            ByteBuffer buffer = ByteBuffer.allocate(getSize());
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            return buffer.array();

        }

        @Override
        public JSONStreamAware getJSON() {

            JSONObject attachment = new JSONObject();

            return attachment;

        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.Messaging.POLL_CREATION;
        }
    }

    public static class MessagingVoteCasting implements Attachment, Serializable {

        static final long serialVersionUID = 0;

        public MessagingVoteCasting() {

        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public byte[] getBytes() {

            ByteBuffer buffer = ByteBuffer.allocate(getSize());
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            return buffer.array();

        }

        @Override
        public JSONStreamAware getJSON() {

            JSONObject attachment = new JSONObject();

            return attachment;

        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.Messaging.VOTE_CASTING;
        }
    }

    public static class ColoredCoinsAssetIssuance implements Attachment, Serializable {

        static final long serialVersionUID = 0;

        private final String name;
        private final String description;
        private final int quantity;

        public ColoredCoinsAssetIssuance(String name, String description, int quantity) {

            this.name = name;
            this.description = description == null ? "" : description;
            this.quantity = quantity;

        }

        @Override
        public int getSize() {
            try {
                return 1 + name.getBytes("UTF-8").length + 2 + description.getBytes("UTF-8").length + 4;
            } catch (RuntimeException|UnsupportedEncodingException e) {
                Logger.logMessage("Error in getBytes", e);
                return 0;
            }
        }

        @Override
        public byte[] getBytes() {

            try {
                byte[] name = this.name.getBytes("UTF-8");
                byte[] description = this.description.getBytes("UTF-8");

                ByteBuffer buffer = ByteBuffer.allocate(1 + name.length + 2 + description.length + 4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.put((byte)name.length);
                buffer.put(name);
                buffer.putShort((short)description.length);
                buffer.put(description);
                buffer.putInt(quantity);

                return buffer.array();
            } catch (RuntimeException|UnsupportedEncodingException e) {
                Logger.logMessage("Error in getBytes", e);
                return null;
            }

        }

        @Override
        public JSONStreamAware getJSON() {

            JSONObject attachment = new JSONObject();
            attachment.put("name", name);
            attachment.put("description", description);
            attachment.put("quantity", quantity);

            return attachment;

        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.ColoredCoins.ASSET_ISSUANCE;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static class ColoredCoinsAssetTransfer implements Attachment, Serializable {

        static final long serialVersionUID = 0;

        private final Long assetId;
        private final int quantity;

        public ColoredCoinsAssetTransfer(Long assetId, int quantity) {

            this.assetId = assetId;
            this.quantity = quantity;

        }

        @Override
        public int getSize() {
            return 8 + 4;
        }

        @Override
        public byte[] getBytes() {

            ByteBuffer buffer = ByteBuffer.allocate(getSize());
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putLong(Convert.nullToZero(assetId));
            buffer.putInt(quantity);

            return buffer.array();

        }

        @Override
        public JSONStreamAware getJSON() {

            JSONObject attachment = new JSONObject();
            attachment.put("asset", Convert.convert(assetId));
            attachment.put("quantity", quantity);

            return attachment;

        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.ColoredCoins.ASSET_TRANSFER;
        }

        public Long getAssetId() {
            return assetId;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    abstract static class ColoredCoinsOrderPlacement implements Attachment, Serializable {

        static final long serialVersionUID = 0;

        private final Long assetId;
        private final int quantity;
        private final long price;

        private ColoredCoinsOrderPlacement(Long assetId, int quantity, long price) {

            this.assetId = assetId;
            this.quantity = quantity;
            this.price = price;

        }

        @Override
        public int getSize() {
            return 8 + 4 + 8;
        }

        @Override
        public byte[] getBytes() {

            ByteBuffer buffer = ByteBuffer.allocate(getSize());
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putLong(Convert.nullToZero(assetId));
            buffer.putInt(quantity);
            buffer.putLong(price);

            return buffer.array();

        }

        @Override
        public JSONStreamAware getJSON() {

            JSONObject attachment = new JSONObject();
            attachment.put("asset", Convert.convert(assetId));
            attachment.put("quantity", quantity);
            attachment.put("price", price);

            return attachment;

        }

        public Long getAssetId() {
            return assetId;
        }

        public int getQuantity() {
            return quantity;
        }

        public long getPrice() {
            return price;
        }
    }

    public static class ColoredCoinsAskOrderPlacement extends ColoredCoinsOrderPlacement {

        static final long serialVersionUID = 0;

        public ColoredCoinsAskOrderPlacement(Long assetId, int quantity, long price) {
            super(assetId, quantity, price);
        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.ColoredCoins.ASK_ORDER_PLACEMENT;
        }

    }

    public static class ColoredCoinsBidOrderPlacement extends ColoredCoinsOrderPlacement {

        static final long serialVersionUID = 0;

        public ColoredCoinsBidOrderPlacement(Long assetId, int quantity, long price) {
            super(assetId, quantity, price);
        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.ColoredCoins.BID_ORDER_PLACEMENT;
        }

    }

    abstract static class ColoredCoinsOrderCancellation implements Attachment, Serializable {

        static final long serialVersionUID = 0;

        private final Long orderId;

        private ColoredCoinsOrderCancellation(Long orderId) {
            this.orderId = orderId;
        }

        @Override
        public int getSize() {
            return 8;
        }

        @Override
        public byte[] getBytes() {

            ByteBuffer buffer = ByteBuffer.allocate(getSize());
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putLong(Convert.nullToZero(orderId));

            return buffer.array();

        }

        @Override
        public JSONStreamAware getJSON() {

            JSONObject attachment = new JSONObject();
            attachment.put("order", Convert.convert(orderId));

            return attachment;

        }

        public Long getOrderId() {
            return orderId;
        }
    }

    public static class ColoredCoinsAskOrderCancellation extends ColoredCoinsOrderCancellation {

        static final long serialVersionUID = 0;

        public ColoredCoinsAskOrderCancellation(Long orderId) {
            super(orderId);
        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.ColoredCoins.ASK_ORDER_CANCELLATION;
        }

    }

    public static class ColoredCoinsBidOrderCancellation extends ColoredCoinsOrderCancellation {

        static final long serialVersionUID = 0;

        public ColoredCoinsBidOrderCancellation(Long orderId) {
            super(orderId);
        }

        @Override
        public Transaction.Type getTransactionType() {
            return Transaction.Type.ColoredCoins.BID_ORDER_CANCELLATION;
        }

    }

}