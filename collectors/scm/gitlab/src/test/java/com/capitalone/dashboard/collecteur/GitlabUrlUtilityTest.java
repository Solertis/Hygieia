package com.capitalone.dashboard.collecteur;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.model.GitlabGitRepo;

@RunWith(MockitoJUnitRunner.class)
public class GitlabUrlUtilityTest {
	
	@Mock
	private GitlabSettings gitlabSettings;
	
	@Mock
	private GitlabGitRepo gitlabRepo;
	
	@InjectMocks
	private GitlabUrlUtility gitlabUrlUtility;

	@Test
	public void shouldBuildApiUrl() {
		when(gitlabRepo.getRepoUrl()).thenReturn("https://domain.org/namespace/Hygieia");
		when(gitlabRepo.getBranch()).thenReturn("master");
		
		URI result  = gitlabUrlUtility.buildApiUrl(gitlabRepo, true, 100);
		
		assertEquals("https", result.getScheme());
		assertEquals("gitlab.com", result.getHost());
		assertEquals("/api/v3/projects/namespace%2FHygieia/repository/commits/", result.getRawPath());
		assertTrue(result.getQuery().contains("ref_name=master"));
		assertTrue(result.getQuery().contains("per_page=100"));
		assertTrue(result.getQuery().contains("since="));
	}
	
	@Test
	public void shouldBuildApiUrlWithGitExtension() {
		when(gitlabRepo.getRepoUrl()).thenReturn("https://domain.org/namespace/Hygieia.git");
		when(gitlabRepo.getBranch()).thenReturn("master");
		
		URI result  = gitlabUrlUtility.buildApiUrl(gitlabRepo, true, 100);
		
		assertEquals("https", result.getScheme());
		assertEquals("gitlab.com", result.getHost());
		assertEquals("/api/v3/projects/namespace%2FHygieia/repository/commits/", result.getRawPath());
		assertTrue(result.getQuery().contains("ref_name=master"));
		assertTrue(result.getQuery().contains("per_page=100"));
		assertTrue(result.getQuery().contains("since="));
	}
	
	@Test
	public void shouldBuildApiUrlWithCustomHost() {
		when(gitlabRepo.getRepoUrl()).thenReturn("https://domain.org/namespace/Hygieia");
		when(gitlabRepo.getBranch()).thenReturn("master");
		when(gitlabSettings.getHost()).thenReturn("customhost.com");
		
		URI result  = gitlabUrlUtility.buildApiUrl(gitlabRepo, true, 100);
		
		assertEquals("https", result.getScheme());
		assertEquals("customhost.com", result.getHost());
		assertEquals("/api/v3/projects/namespace%2FHygieia/repository/commits/", result.getRawPath());
		assertTrue(result.getQuery().contains("ref_name=master"));
		assertTrue(result.getQuery().contains("per_page=100"));
		assertTrue(result.getQuery().contains("since="));
	}
	
	@Test
	public void shouldBuildApiUrlForFirstRunProvidedHistoryDays() {
		when(gitlabRepo.getRepoUrl()).thenReturn("https://domain.org/namespace/Hygieia");
		when(gitlabRepo.getBranch()).thenReturn("master");
		when(gitlabSettings.getFirstRunHistoryDays()).thenReturn(10);
		
		URI result  = gitlabUrlUtility.buildApiUrl(gitlabRepo, true, 100);
		
		assertEquals("https", result.getScheme());
		assertEquals("gitlab.com", result.getHost());
		assertEquals("/api/v3/projects/namespace%2FHygieia/repository/commits/", result.getRawPath());
		assertTrue(result.getQuery().contains("ref_name=master"));
		assertTrue(result.getQuery().contains("per_page=100"));
		assertTrue(result.getQuery().contains("since="));
	}
	
	@Test
	public void shouldBuildApiUrlForNonFirstRun() {
		when(gitlabRepo.getRepoUrl()).thenReturn("https://domain.org/namespace/Hygieia");
		when(gitlabRepo.getBranch()).thenReturn("master");
		when(gitlabRepo.getLastUpdated()).thenReturn(1477513100920L);
		
		URI result  = gitlabUrlUtility.buildApiUrl(gitlabRepo, false, 100);
		
		assertEquals("https", result.getScheme());
		assertEquals("gitlab.com", result.getHost());
		assertEquals("/api/v3/projects/namespace%2FHygieia/repository/commits/", result.getRawPath());
		assertTrue(result.getQuery().contains("ref_name=master"));
		assertTrue(result.getQuery().contains("per_page=100"));
		assertTrue(result.getQuery().contains("since="));
	}
	
	@Test
	public void shouldUpdatePage() throws URISyntaxException {
		URI uri = new URI("http://fakeurl.com");
		URI result  = gitlabUrlUtility.updatePage(uri, 2);
		
		assertTrue(result.getQuery().contains("page=2"));
	}

}
