package it.chalmers.gamma.requests;

import it.chalmers.gamma.db.entity.Text;
import it.chalmers.gamma.domain.GroupType;

import java.util.List;
import java.util.Objects;

public class CreateGroupRequest {
    private String name;
    private String prettyName;
    private Text description;
    private String email;
    private GroupType type;
    private Text func;
    private String avatarURL;
    private List<WebsiteInfo> websites;

    public List<WebsiteInfo> getWebsites() {
        return this.websites;
    }

    public void setWebsites(List<WebsiteInfo> websites) {
        this.websites = websites;
    }


    public void setDescription(Text description) {
        this.description = description;
    }

    public Text getDescription() {
        return this.description;
    }

    public Text getFunc() {
        return this.func;
    }

    public void setFunc(Text func) {
        this.func = func;
    }

    public String getAvatarURL() {
        return this.avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GroupType getType() {
        return this.type;
    }

    public String getPrettyName() {
        return this.prettyName;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateGroupRequest that = (CreateGroupRequest) o;
        return this.name.equals(that.name)
            && this.prettyName.equals(that.prettyName)
            && this.description.equals(that.description)
            && this.email.equals(that.email)
            && this.type == that.type
            && this.func.equals(that.func)
            && this.avatarURL.equals(that.avatarURL)
            && this.websites.equals(that.websites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.prettyName, this.description, this.email,
            this.type, this.func, this.avatarURL, this.websites);
    }

    @Override
    public String toString() {
        return "CreateGroupRequest{"
            + "name='" + this.name + '\''
            + ", prettyName='" + this.prettyName + '\''
            + ", description=" + this.description
            + ", email='" + this.email + '\''
            + ", type=" + this.type
            + ", func=" + this.func
            + ", avatarURL='" + this.avatarURL + '\''
            + ", websites=" + this.websites
            + '}';
    }

    public static class WebsiteInfo {
        String website;
        String url;

        public String getWebsite() {
            return this.website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WebsiteInfo that = (WebsiteInfo) o;
            return this.website.equals(that.website)
                && this.url.equals(that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.website, this.url);
        }

        @Override
        public String toString() {
            return "WebsiteInfo{"
                + "website='" + this.website + '\''
                + ", url='" + this.url + '\''
                + '}';
        }
    }
}