package com.my.list.service.filter;

import com.my.list.domain.MainData;
import com.my.list.dto.Node;
import com.my.list.service.data.ListService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filter {
    
    public boolean filter(Node node) {
        MainData mainData = node.getMainData();
        return (
            (this.part == null || this.part == mainData.getPart()) 
                && (this.collection == null || this.collection == mainData.getCollection())
                && (this.nsfw == null || this.nsfw == mainData.getNsfw())
                && (this.like == null || this.like == mainData.getLike())
                && (this.hide == null || this.hide == mainData.getHide())
                && (this.types == null || this.types.contains(mainData.getType()))
        );
    }

    Boolean cascade = null;
    Boolean part = null;
    Boolean collection = null;
    public Boolean getCascade() {
        return cascade;
    }
    public void setCascade(Boolean cascade) {
        this.cascade = cascade;
    }
    public Boolean getPart() {
        return part;
    }
    public void setPart(Boolean part) {
        this.part = part;
    }
    public Boolean getCollection() {
        return collection;
    }
    public void setCollection(Boolean collection) {
        this.collection = collection;
    }
    
    List<String> types = null;
    public List<String> getTypes() {
        return types;
    }
    public void setTypes(List<String> types) {
        this.types = types;
    }

    List<Condition> conditions = null;
    public Filter addCondition(Condition condition) {
        if (conditions == null) conditions = new ArrayList<>();
        conditions.add(condition);
        return this;
    }
    public Filter addCondition(String column, String oper, Object value) {
        return addCondition(new Condition(column, oper, value));
    }

    List<Sort> sorts = null;
    public Filter addSort(Sort sort) {
        if (sorts == null) sorts = new ArrayList<>();
        sorts.add(sort);
        return this;
    }
    public Filter addSort(String property, Sort.Direction direction) {
        return addSort(new Sort(property, direction));
    }
    public Filter addSort(String property) {
        return addSort(property, Sort.Direction.ASC);
    }

    Permission permission = null;
    Boolean nsfw = null;
    Boolean like = null;
    Boolean hide = null;
    public Filter setPermission(Permission permission) {
        this.permission = permission;
        return this;
    }
    public Filter setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }
    public Filter setLike(Boolean like) {
        this.like = like;
        return this;
    }
    public Filter setHide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    Set<Tag> andTags = null;
    Set<Tag> orTags = null;
    Set<Tag> notTags = null;
    public Filter addAndTag(Tag tag) {
        if (andTags == null) andTags = new HashSet<>();
        andTags.add(tag);
        return this;
    }
    public Filter addOrTag(Tag tag) {
        if (orTags == null) orTags = new HashSet<>();
        orTags.add(tag);
        return this;
    }
    public Filter addNotTag(Tag tag) {
        if (notTags == null) notTags = new HashSet<>();
        notTags.add(tag);
        return this;
    }

    Set<String> andWords = null;
    Set<String> orWords = null;
    Set<String> notWords = null;
    public Filter addAndWord(String word) {
        if (andWords == null) andWords = new HashSet<>();
        andWords.add(word);
        return this;
    }
    public Filter addOrWord(String word) {
        if (orWords == null) orWords = new HashSet<>();
        orWords.add(word);
        return this;
    }
    public Filter addNotWord(String word) {
        if (notWords == null) notWords = new HashSet<>();
        notWords.add(word);
        return this;
    }
    
    public List<Node> getAll(ListService listService) {
        return listService.getAll(this);
    }

    // ---- Setter & Getter ---- //
    public List<Condition> getConditions() {
        return conditions;
    }
    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
    public List<Sort> getSorts() {
        return sorts;
    }
    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }
    public Permission getPermission() {
        return permission;
    }
    public Boolean getNsfw() {
        return nsfw;
    }
    public Boolean getLike() {
        return like;
    }
    public Boolean getHide() {
        return hide;
    }
    public Set<Tag> getAndTags() {
        return andTags;
    }
    public void setAndTags(Set<Tag> andTags) {
        this.andTags = andTags;
    }
    public Set<Tag> getOrTags() {
        return orTags;
    }
    public void setOrTags(Set<Tag> orTags) {
        this.orTags = orTags;
    }
    public Set<Tag> getNotTags() {
        return notTags;
    }
    public void setNotTags(Set<Tag> notTags) {
        this.notTags = notTags;
    }
    public Set<String> getAndWords() {
        return andWords;
    }
    public void setAndWords(Set<String> andWords) {
        this.andWords = andWords;
    }
    public Set<String> getOrWords() {
        return orWords;
    }
    public void setOrWords(Set<String> orWords) {
        this.orWords = orWords;
    }
    public Set<String> getNotWords() {
        return notWords;
    }
    public void setNotWords(Set<String> notWords) {
        this.notWords = notWords;
    }
}
