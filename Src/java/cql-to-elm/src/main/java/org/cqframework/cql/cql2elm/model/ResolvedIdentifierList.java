package org.cqframework.cql.cql2elm.model;

import org.hl7.elm.r1.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic utility for maintaining a list of objects representing resolved identifiers, categorized by case sensitive matching
 */
public class ResolvedIdentifierList {

    private final List<ResolvedIdentifier> list;

    public ResolvedIdentifierList() {
        this.list = new ArrayList<>();
    }

    public List<ResolvedIdentifier> getList() {
        return this.list;
    }

    public void addResolvedIdentifier(ResolvedIdentifier ri) {
        this.list.add(ri);
    }

    /**
     * Establish match type and add new instance of ResolvedIdentifier, regardless of case matching
     *
     * @param identifier         Visible identifier name to use
     * @param val                Value of identifier used in case matching
     * @param checkVal           Value to be compared against
     * @param resolvedIdentifier Object to record to the list
     */
    public void addResolvedIdentifier(String identifier, String val, String checkVal, Object resolvedIdentifier) {
        this.list.add(new ResolvedIdentifier(identifier, MatchType.resolveMatchType(val, checkVal), resolvedIdentifier));
    }

    /**
     * Match type set to EXACT and new instance of ResolvedIdentifier added to list
     *
     * @param identifier identifier of resolved element
     * @param resolvedElement resolved element
     */
    public void addExactMatchIdentifier(String identifier, Object resolvedElement) {
        this.list.add(new ResolvedIdentifier(identifier, MatchType.EXACT, resolvedElement));
    }

    /**
     * Combine list from another instance of ResolvedIdentifierList
     *
     * @param m instance of ResolvedIdentifierList
     */
    public void addAllResolvedIdentifiers(ResolvedIdentifierList m) {
        this.list.addAll(m.getList());
    }

    /**
     * Returns first instance in the list where MatchType is EXACT.  List is ordered in first come first serve basis where first EXACT match is what is ultimately used.
     *
     * @return ResolvedIdentifier
     */
    public ResolvedIdentifier firstInstanceOfExactMatch() {
        return this.list.stream()
                .filter(s -> s.getMatchType().equals(MatchType.EXACT))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns first instance in list where MatchType is EXACT and the object can resolve to an Expression.
     *
     * @return ResolvedIdentifier
     */
    public ResolvedIdentifier firstInstanceOfExactMatchExpression() {
        return this.list.stream()
                .filter(s -> s.getMatchType().equals(MatchType.EXACT) && canResolveToExpression(s.getResolvedElement()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns list of ResolvedIdentifiers based on MatchType
     * @param matchType MatchType passed in
     * @return List of ResolvedIdentifier
     */
    public List<ResolvedIdentifier> findAllMatchesByType(MatchType matchType) {
        return this.list.stream()
                .filter(s -> s.getMatchType().equals(matchType))
                .collect(Collectors.toList());
    }

    private boolean canResolveToExpression(Object in) {
        try {
            Expression e = (Expression) in;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return Number of ResolvedIdentifiers
     */
    public int size() {
        return this.list.size();
    }
}
