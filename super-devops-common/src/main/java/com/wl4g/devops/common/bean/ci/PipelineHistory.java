package com.wl4g.devops.common.bean.ci;

import com.wl4g.devops.common.bean.BaseBean;

import java.util.List;

public class PipelineHistory extends BaseBean {
    private static final long serialVersionUID = 6815608076300843748L;

    private Integer pipeId;

    private String providerKind;

    private Integer status;

    private String shaLocal;

    private Integer refId;

    private Long costTime;

    private String trackType;

    private String trackId;

    private String annex;

    //other
    private String pipeName;

    private String clusterName;

    private String createByName;

    private List<PipelineHistoryInstance> pipelineHistoryInstances;

    public Integer getPipeId() {
        return pipeId;
    }

    public void setPipeId(Integer pipeId) {
        this.pipeId = pipeId;
    }

    public String getProviderKind() {
        return providerKind;
    }

    public void setProviderKind(String providerKind) {
        this.providerKind = providerKind == null ? null : providerKind.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getShaLocal() {
        return shaLocal;
    }

    public void setShaLocal(String shaLocal) {
        this.shaLocal = shaLocal == null ? null : shaLocal.trim();
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId == null ? null : trackId.trim();
    }

    public String getAnnex() {
        return annex;
    }

    public void setAnnex(String annex) {
        this.annex = annex == null ? null : annex.trim();
    }

    public String getPipeName() {
        return pipeName;
    }

    public void setPipeName(String pipeName) {
        this.pipeName = pipeName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public List<PipelineHistoryInstance> getPipelineHistoryInstances() {
        return pipelineHistoryInstances;
    }

    public void setPipelineHistoryInstances(List<PipelineHistoryInstance> pipelineHistoryInstances) {
        this.pipelineHistoryInstances = pipelineHistoryInstances;
    }
}