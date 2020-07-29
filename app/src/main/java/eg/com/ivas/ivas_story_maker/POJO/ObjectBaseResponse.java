package eg.com.ivas.ivas_story_maker.POJO;

import com.google.gson.annotations.SerializedName;

public class ObjectBaseResponse<T> {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private T response;

    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
