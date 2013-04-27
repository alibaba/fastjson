package data.media;

import java.util.List;

@SuppressWarnings("serial")
public class MediaContent implements java.io.Serializable
{
	public Media media;
	public List<Image> images;

	public MediaContent() {}

	public MediaContent (Media media, List<Image> images) {
		this.media = media;
		this.images = images;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MediaContent that = (MediaContent) o;

		if (images != null ? !images.equals(that.images) : that.images != null) return false;
		if (media != null ? !media.equals(that.media) : that.media != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = media != null ? media.hashCode() : 0;
		result = 31 * result + (images != null ? images.hashCode() : 0);
		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[MediaContent: ");
		sb.append("media=").append(media);
		sb.append(", images=").append(images);
		sb.append("]");
		return sb.toString();
	}

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Media getMedia() {
        return media;
    }

    public List<Image> getImages() {
        return images;
    }
}
