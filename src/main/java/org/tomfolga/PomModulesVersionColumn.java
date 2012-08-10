package org.tomfolga;

import hudson.Extension;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.Descriptor;
import hudson.views.ListViewColumn;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * This class defines a column that shows Maven POM Modules groupId, artifactId
 * and version.
 *
 * This class has multiple descriptors which instantiate this class with
 * different params. I am not sure if it's recommended way in Hudson, but it
 * allows to create multiple column definition using one class/jelly file. TODO:
 * check if/how columns can be configured to avoid multiple columns
 *
 * @author tomfolga
 *
 */
public class PomModulesVersionColumn extends AbstractPomModulesColumn {

	public Descriptor<ListViewColumn> getDescriptor() {
		return DESCRIPTOR;
	}

	public static final Descriptor<ListViewColumn> DESCRIPTOR = new DescriptorImpl();

	public PomModulesVersionColumn(String columnName) {
		super(columnName, false, false, true);
	}

	public String uniqueVersionsString(Object lastBuild) {
		SortedSet<String> result = new TreeSet<String>();

		if (lastBuild instanceof MavenModuleSetBuild) {
			MavenModuleSetBuild moduleSetBuild = (MavenModuleSetBuild) lastBuild;
			for (MavenModule module : moduleSetBuild.getModuleLastBuilds().keySet()) {
				result.add(module.getVersion());
			}
		}

		return StringUtils.join(result, ", ");
	}

	@Extension
	public static class DescriptorImpl extends Descriptor<ListViewColumn> {
		@Override
		public ListViewColumn newInstance(StaplerRequest req,
				JSONObject formData) throws FormException {

			return new PomModulesVersionColumn(getDisplayName());
		}

		@Override
		public String getDisplayName() {
			return "Maven Versions";
		}
	}
}
